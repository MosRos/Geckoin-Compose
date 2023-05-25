package com.mrostami.geckoincompose.domain.usecases

import androidx.paging.PagingData
import com.mrostami.geckoincompose.domain.AllCoinsRepository
import com.mrostami.geckoincompose.model.Coin
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SearchCoinsUseCase @Inject constructor(
    private val allCoinsRepository: AllCoinsRepository
) {
    operator fun invoke(
        query: String?
    ): Flow<PagingData<Coin>> {
        return allCoinsRepository.searchCoins(searchInput = query)
    }
}