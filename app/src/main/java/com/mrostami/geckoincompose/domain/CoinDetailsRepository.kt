package com.mrostami.geckoincompose.domain

import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.model.CoinDetailsInfo
import com.mrostami.geckoincompose.model.SimplePriceInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface CoinDetailsRepository {
    fun getCoinDetails(coinId: String) : Flow<Result<CoinDetailsInfo>> = flow {  }
    fun getSimplePriceInfo(coinId: String) : Flow<Result<SimplePriceInfo>> = flow {  }
}