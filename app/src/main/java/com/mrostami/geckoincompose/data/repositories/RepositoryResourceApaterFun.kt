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

 fun <P : Any?, T : Any, R : Any> repositoryAdapter(
    request: P,
    networkRequest: suspend P.() -> Either<CoinGeckoApiError, T>,
    responseMapper: ((T) -> R)? = null,
    dbReader: (suspend P.() -> R?)? = null,
    dbWriter: (suspend (R) -> Unit)? = null,
    forceRefresh: Boolean = false,
    rateLimiter: Long = 5000,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
): Flow<Result<R>> = flow {
    var lastRequestTime: Long = 0L

    val cachedData: R? = dbReader?.invoke(request)
    if (cachedData != null) {
        emit(Result.Success(cachedData))
    }

    if (NetworkUtils.isConnected()) {
        if (isNotLimited(lastRequestTime, rateLimiter, forceRefresh) || forceRefresh) {
            emit(Result.Loading)
            try {
                val response = networkRequest.invoke(request)
                response.onRight {
                    val data: R? = responseMapper?.invoke(it)
                    if (data != null) {
                        dbWriter?.invoke(data)
                    }
                    val refreshedData: R? = dbReader?.invoke(request)
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
//        emit(Result.Error(Exception("No internet connection"), message = "No internet connection"))
    }
}.catch { e ->
    emit(Result.Error(Exception(e)))
}.flowOn(coroutineDispatcher)


private fun isNotLimited(
    lastRequestTime: Long,
    rateLimiter: Long,
    forceRefresh: Boolean
): Boolean {
    val trigger: Long = System.currentTimeMillis() - lastRequestTime
    return forceRefresh || trigger > rateLimiter
}
