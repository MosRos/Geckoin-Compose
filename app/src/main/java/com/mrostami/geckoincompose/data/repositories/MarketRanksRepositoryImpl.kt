/*
 * *
 *  * Created by Moslem Rostami on 6/17/20 5:04 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/17/20 5:04 PM
 *
 */

package com.mrostami.geckoincompose.data.repositories

import androidx.paging.*
import com.mrostami.geckoincompose.data.local.LocalDataSource
import com.mrostami.geckoincompose.data.remote.RemoteDataSource
import com.mrostami.geckoincompose.domain.MarketRanksRepository
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.model.RankedCoin
import com.mrostami.geckoincompose.model.TrendCoin
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class MarketRanksRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val marketRanksMediator: MarketRanksMediator,
    private val networkMarketRanksPagingSource: NetworkMarketRanksPagingSource
) : MarketRanksRepository {


    override fun getCoinsList(offset: Int, limit: Int): Flow<Result<List<RankedCoin>>> = repositoryAdapter(
        request = null,
        networkRequest = {
            remoteDataSource.getPagedMarketRanks(offset = offset, limit = limit)
        },
        responseMapper = {
            it
        },
        dbReader = {
            localDataSource.getRankedCoinsList(offset = offset, limit = limit)
        },
        dbWriter = { coins ->
            saveRanks(coins, offset)
        },
        forceRefresh = true,
    )

    private suspend fun saveRanks(coins: List<RankedCoin>, page: Int) {
        localDataSource.insertRankedCoins(coins.map {
            it.apply { pageKey = page }
        })
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getRanks(): Flow<PagingData<RankedCoin>> {

        val pagingSourceFactory = { localDataSource.getPagedRankedCoins() }
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = 1,
                enablePlaceholders = true,
                initialLoadSize = 50,
                maxSize = 200
            ),
            initialKey = 1,
            remoteMediator = marketRanksMediator,
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}