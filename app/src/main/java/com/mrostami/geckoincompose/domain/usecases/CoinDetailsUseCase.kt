package com.mrostami.geckoincompose.domain.usecases

import com.mrostami.geckoincompose.domain.CoinDetailsRepository
import com.mrostami.geckoincompose.domain.base.FlowUseCase
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.model.CoinDetailsInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CoinDetailsUseCase @Inject constructor(
    private val coinDetailsRepository: CoinDetailsRepository
) : FlowUseCase<String, CoinDetailsInfo>(coroutineDispatcher = Dispatchers.IO) {
    override fun execute(parameters: String, refresh: Boolean): Flow<Result<CoinDetailsInfo>> {
        return coinDetailsRepository.getCoinDetails(coinId = parameters)
    }
}