package com.mrostami.geckoincompose.domain

import androidx.paging.PagingData
import com.mrostami.geckoincompose.model.RankedCoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface MarketRanksRepository {
    fun getRanks() : Flow<PagingData<RankedCoin>> = flow {  }
}