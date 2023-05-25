package com.mrostami.geckoincompose.domain.usecases

import com.mrostami.geckoincompose.domain.GlobalInfoRepository
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.domain.base.SimpleFlowUseCase
import com.mrostami.geckoincompose.model.BitcoinPriceInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BitcoinSimplePriceUseCase @Inject constructor(
    private val globalInfoRepository: GlobalInfoRepository
) : SimpleFlowUseCase<BitcoinPriceInfo>(coroutineDispatcher = Dispatchers.IO) {

    override fun execute(refresh: Boolean): Flow<Result<BitcoinPriceInfo>> {
        return globalInfoRepository.getBtcDailyPriceInfo(forceRefresh = refresh)
    }
}