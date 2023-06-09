package com.mrostami.geckoincompose.domain.usecases

import com.mrostami.geckoincompose.data.repositories.PAGE_SIZE
import com.mrostami.geckoincompose.domain.MarketRanksRepository
import com.mrostami.geckoincompose.domain.base.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InitRankedCoinsPagingUseCase @Inject constructor(
    private val marketRanksRepository: MarketRanksRepository
) {
    operator fun invoke(initSize: Int = PAGE_SIZE*2) : Flow<Result<Boolean>> = marketRanksRepository.getAndCacheInitRanks(initSize)
}