package com.mrostami.geckoincompose.domain.usecases

import com.mrostami.geckoincompose.domain.GlobalInfoRepository
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.domain.base.SimpleFlowUseCase
import com.mrostami.geckoincompose.model.GlobalMarketInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GlobalMarketInfoUseCase @Inject constructor(
    private val globalInfoRepository: GlobalInfoRepository
) : SimpleFlowUseCase<GlobalMarketInfo>(coroutineDispatcher = Dispatchers.IO) {

    override fun execute(refresh: Boolean): Flow<Result<GlobalMarketInfo>> {
        return globalInfoRepository.getMarketInfo(forceRefresh = refresh)
    }
}