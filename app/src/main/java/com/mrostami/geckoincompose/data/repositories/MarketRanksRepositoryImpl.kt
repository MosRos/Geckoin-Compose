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
import com.mrostami.geckoincompose.domain.MarketRanksRepository
import com.mrostami.geckoincompose.model.RankedCoin
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class MarketRanksRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val marketRanksMediator: MarketRanksMediator,
    private val networkMarketRanksPagingSource: NetworkMarketRanksPagingSource
) : MarketRanksRepository {


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
//            remoteMediator = marketRanksMediator,
            pagingSourceFactory = {
                networkMarketRanksPagingSource
            }
        ).flow
    }
}