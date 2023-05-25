package com.mrostami.geckoincompose.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.mrostami.geckoincompose.data.local.LocalDataSource
import com.mrostami.geckoincompose.data.remote.RemoteDataSource
import com.mrostami.geckoincompose.domain.AllCoinsRepository
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.model.Coin
import com.mrostami.geckoincompose.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AllCoinsRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : AllCoinsRepository {

    companion object {
        const val COINS_PAGE_SIZE = 50
    }

    private val syncRateLimiter = 7 * 24 * 60 * 60 * 1000L
    private val pageConfig = PagingConfig(
        pageSize = COINS_PAGE_SIZE,
        prefetchDistance = 5,
        enablePlaceholders = true,
        initialLoadSize = 100,
        maxSize = 200
    )
    private val coinsPagingSourceFactory = { localDataSource.getPagedCoins() }
    private fun getSearchPagingSourceFactory(query: String): () -> PagingSource<Int, Coin> =
        { localDataSource.searchPagedCoins(input = query) }


    override fun syncAllCoins(forceRefresh: Boolean): Flow<Result<Boolean>> {
        return flow {
            if (NetworkUtils.isConnected()) {
                if (isNotLimited() || forceRefresh) {
                    val result = remoteDataSource.getAllCoins()
                    result.onRight {
                        saveCoins(it)
                    }
                    result.onLeft {
                        emit(Result.Error(Exception(it.error)))
                    }
                } else {
                    emit(Result.Success(true))
                }

            } else {
                emit(Result.Error(Exception("No internet connection")))
            }
        }
    }

    override fun searchCoins(searchInput: String?): Flow<PagingData<Coin>> {
        val pagingSourceFactory =
            if (searchInput.isNullOrEmpty())
                coinsPagingSourceFactory
            else
                getSearchPagingSourceFactory(searchInput)

        return Pager(
            config = pageConfig,
            initialKey = 1,
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    private fun isNotLimited(): Boolean {
        val lastSyncDate: Long = localDataSource.getLastSyncDate()
        return System.currentTimeMillis() - lastSyncDate > syncRateLimiter
    }

    suspend fun saveCoins(coins: List<Coin>) {
        withContext(Dispatchers.IO) {
            localDataSource.insertCoins(coins)
        }
        localDataSource.setLastSyncDate(System.currentTimeMillis())
    }
}