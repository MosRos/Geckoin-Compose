package com.mrostami.geckoincompose.domain.usecases

import com.mrostami.geckoincompose.domain.CoinDetailsRepository
import com.mrostami.geckoincompose.domain.base.FlowUseCase
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.model.SimplePriceInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SimplePriceUseCase @Inject constructor(
    private val coinDetailsRepository: CoinDetailsRepository
) : FlowUseCase<String, SimplePriceInfo>(coroutineDispatcher = Dispatchers.IO){
    override fun execute(parameters: String, refresh: Boolean): Flow<Result<SimplePriceInfo>> {
        return coinDetailsRepository.getSimplePriceInfo(coinId = parameters)
    }
}