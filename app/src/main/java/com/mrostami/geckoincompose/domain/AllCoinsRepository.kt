package com.mrostami.geckoincompose.domain

import androidx.paging.PagingData
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.model.Coin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface AllCoinsRepository {
    fun syncAllCoins(forceRefresh: Boolean) : Flow<Result<Boolean>> = flow { }
    fun searchCoins(searchInput: String?): Flow<PagingData<Coin>> = flow { }
}