package com.mrostami.geckoincompose.domain

import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.model.BitcoinPriceInfo
import com.mrostami.geckoincompose.model.GlobalMarketInfo
import com.mrostami.geckoincompose.model.PriceEntry
import com.mrostami.geckoincompose.model.TrendCoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface GlobalInfoRepository {
    fun getMarketInfo(forceRefresh: Boolean) : Flow<Result<GlobalMarketInfo>> = flow {  }
    fun getTrendingCoins(forceRefresh: Boolean) : Flow<Result<List<TrendCoin>>> = flow {  }
    fun getBtcDailyPriceInfo(forceRefresh: Boolean) : Flow<Result<BitcoinPriceInfo>> = flow {  }
    fun getBtcMarketChartInfo(forceRefresh: Boolean) : Flow<Result<List<PriceEntry>>> = flow {  }
}