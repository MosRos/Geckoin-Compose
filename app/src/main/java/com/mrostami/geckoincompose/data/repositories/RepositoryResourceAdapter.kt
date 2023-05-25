package com.mrostami.geckoincompose.data.repositories

import arrow.core.Either
import com.mrostami.geckoin.data.remote.responses.CoinGeckoApiError
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.utils.NetworkUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Represents a resource which needs to be loaded from the network and persisted to the Database
 *
 * @param P the input param for network request
 * @param T The type of the entity to be fetched/stored in the database
 * @param U The success type of [NetworkResponse]
 * @param V The error type of [NetworkResponse]
 *
 * The items emitted from this class are wrapped in a [Result] class. The items are emitted in a [Flow].
 * Following is sequence of actions taken:
 * 1. Emit Resource.Loading
 * 2. Query database for cached data using [getFromDatabase].
 * 3. Emit Cached data from the database, if there is any. The decision to emit data is made using the abstract
 * [validateCache] method.
 * 4. Fetch data from the API using [getFromApi]
 * 5. If the fetch is successful, persist the data using [persistData] else emit [Result.Error] with the cached data and terminate
 * 6. Emit [Result.Success] with the newly persisted data if the fetch was successful
 *
 */

abstract class RepositoryResourceAdapter<in P : Any?, T : Any?, U : Any, V : Any>(
    private val forceRefresh: Boolean = false,
    val rateLimiter: Long = 5000,
    val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    abstract suspend fun getFromDatabase(): T?
    abstract suspend fun validateCache(cachedData: T?): Boolean
    abstract fun shouldFetchFromApi() : Boolean
    abstract suspend fun getFromApi(): Either<V, U>
    abstract suspend fun persistData(apiData: U)

    operator fun invoke(parameters: P) : Flow<Result<T>> = execute(parameters)
        .catch { e -> emit(Result.Error(Exception(e))) }
        .flowOn(coroutineDispatcher)

    open fun execute(parameters: P) : Flow<Result<T>> = flow {
        val cachedData = getFromDatabase()

        if (validateCache(cachedData) && cachedData != null) {
            emit(Result.Success(cachedData))
        } else {
            emit(Result.Empty)
        }

        if (shouldFetchFromApi() || forceRefresh) {
            if (NetworkUtils.isConnected()) {
                emit(Result.Loading)
                val apiResponse = getFromApi()
                apiResponse.onRight {
                    persistData(it)
                    val refreshedData = getFromDatabase()
                    if (validateCache(refreshedData)) {
                        emit(Result.Success(refreshedData!!))
                    } else {
                        emit(Result.Error(Exception("Oops!"), message = "Failed to load cached data"))
                    }
                }
                apiResponse.onLeft {
                    emit(Result.Error(Exception(it.toString())))
                }
            } else {
                delay(200L)
                emit(Result.Error(Exception("No internet connection"), message = "No internet connection"))
            }
        }
    }
}

abstract class RepositoryAdapter<in P: Any?, T: Any, R: Any>(
    val networkRequest: (parameters: P) -> Either<CoinGeckoApiError, T>,
    val responseMapper: ((T) -> R)? = null,
    val dbReader: ((parameters: P) -> R)? = null,
    val dbWriter: ((R) -> Unit)? = null,
    val forceRefresh: Boolean = false,
    val rateLimiter: Long = 5000,
    val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private var lastRequestTime: Long = 0L

    operator fun invoke(parameters: P) : Flow<Result<R>> = flow {

        val cachedData: R? = dbReader?.invoke(parameters)
        if (cachedData != null) {
            emit(Result.Success(cachedData))
        }

        emit(Result.Loading)
        if (NetworkUtils.isConnected()) {
            if (notLimited() || forceRefresh) {
                try {
                    val response = networkRequest.invoke(parameters)
                    response.onRight {
                        val data: R? = responseMapper?.invoke(it)
                        if (data != null) {
                            dbWriter?.invoke(data)
                        }
                        val refreshedData: R? = dbReader?.invoke(parameters)
                        if (refreshedData != null) {
                            emit(Result.Success(refreshedData))
                        } else {
                            emit(
                                Result.Error(
                                    Exception("Oops!"),
                                    message = "Failed to load cached data"
                                )
                            )
                        }
                    }
                    response.onLeft {
                        emit(Result.Error(Exception(it.error)))
                    }
                } catch (e: Exception) {
                    emit(Result.Error(exception = e))
                }
            }
        } else {
            delay(200)
            emit(Result.Error(Exception("No internet connection"), message = "No internet connection"))
        }
    }.catch { e ->
        emit(Result.Error(Exception(e)))
    }.flowOn(coroutineDispatcher)

    private fun notLimited() : Boolean {
        val trigger: Long = System.currentTimeMillis() - lastRequestTime
        return forceRefresh || trigger > rateLimiter
    }
}