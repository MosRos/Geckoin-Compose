package com.mrostami.geckoincompose.domain.usecases

import com.mrostami.geckoincompose.domain.GlobalInfoRepository
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.domain.base.SimpleFlowUseCase
import com.mrostami.geckoincompose.model.TrendCoin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TrendCoinsUseCase @Inject constructor(
    private val globalInfoRepository: GlobalInfoRepository
) : SimpleFlowUseCase<List<TrendCoin>>(coroutineDispatcher = Dispatchers.IO){
    override fun execute(refresh: Boolean): Flow<Result<List<TrendCoin>>> {
        return globalInfoRepository.getTrendingCoins(forceRefresh = refresh)
    }
}