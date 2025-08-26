package com.mrostami.geckoincompose.domain

import androidx.compose.ui.geometry.Offset
import androidx.paging.PagingData
import arrow.core.Either
import com.mrostami.geckoin.data.remote.responses.CoinGeckoApiError
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.model.RankedCoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface MarketRanksRepository {

    fun getCoinsList(offset: Int, limit: Int) : Flow<Result<List<RankedCoin>>>
    fun getRanks() : Flow<PagingData<RankedCoin>> = flow {  }
}