package com.mrostami.geckoincompose.domain.usecases

import com.mrostami.geckoincompose.data.repositories.PAGE_SIZE
import com.mrostami.geckoincompose.domain.MarketRanksRepository
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.model.RankedCoin
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRankedCoinsUseCase @Inject constructor(
    private val marketRanksRepository: MarketRanksRepository
) {
    operator fun invoke(offset: Int, limit: Int) : Flow<Result<List<RankedCoin>>> = marketRanksRepository.getCoinsList(offset = offset, limit = limit)
}