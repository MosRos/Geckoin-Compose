package com.mrostami.geckoincompose.domain.usecases

import com.mrostami.geckoincompose.domain.PriceHistoryRepository
import com.mrostami.geckoincompose.domain.base.FlowUseCase
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.model.PriceEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PriceChartUseCase @Inject constructor(
    private val priceHistoryRepository: PriceHistoryRepository
) : FlowUseCase<String, List<PriceEntry>>(coroutineDispatcher = Dispatchers.IO) {
    override fun execute(parameters: String, refresh: Boolean): Flow<Result<List<PriceEntry>>> {
        return priceHistoryRepository.getPriceHistory(coinId = parameters)
    }
}