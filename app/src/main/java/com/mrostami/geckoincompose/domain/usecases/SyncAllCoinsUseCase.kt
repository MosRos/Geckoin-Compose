package com.mrostami.geckoincompose.domain.usecases

import com.mrostami.geckoincompose.domain.AllCoinsRepository
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.domain.base.SimpleFlowUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SyncAllCoinsUseCase @Inject constructor(
    private val allCoinsRepository: AllCoinsRepository
) : SimpleFlowUseCase<Boolean>(coroutineDispatcher = Dispatchers.IO) {
    override fun execute(refresh: Boolean): Flow<Result<Boolean>> {
        return allCoinsRepository.syncAllCoins(forceRefresh = refresh)
    }
}