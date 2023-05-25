package com.mrostami.geckoincompose.domain.usecases

import com.mrostami.geckoincompose.domain.GlobalInfoRepository
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.domain.base.SimpleFlowUseCase
import com.mrostami.geckoincompose.model.PriceEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BitcoinChartInfoUseCase @Inject constructor(
    private val globalInfoRepository: GlobalInfoRepository
) : SimpleFlowUseCase<List<PriceEntry>>(coroutineDispatcher = Dispatchers.IO) {

    override fun execute(refresh: Boolean): Flow<Result<List<PriceEntry>>> {
        return globalInfoRepository.getBtcMarketChartInfo(forceRefresh = refresh)
    }
}