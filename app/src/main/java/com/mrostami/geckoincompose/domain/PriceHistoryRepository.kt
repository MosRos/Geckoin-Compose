package com.mrostami.geckoincompose.domain

import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.model.PriceEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface PriceHistoryRepository {
    fun getPriceHistory(
        coinId: String
    ) : Flow<Result<List<PriceEntry>>> = flow {  }
}