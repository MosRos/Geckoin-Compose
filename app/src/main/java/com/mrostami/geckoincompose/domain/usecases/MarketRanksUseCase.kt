/*
 * *
 *  * Created by Moslem Rostami on 6/17/20 5:09 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/17/20 5:09 PM
 *
 */

package com.mrostami.geckoincompose.domain.usecases

import androidx.paging.PagingData
import com.mrostami.geckoincompose.domain.MarketRanksRepository
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.model.RankedCoin
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MarketRanksUseCase @Inject constructor(
    private val marketRanksRepository: MarketRanksRepository
    ) {
    operator fun invoke(
        coroutineDispatcher: CoroutineDispatcher
    ): Flow<Result<PagingData<RankedCoin>>> {
        return flow {
            emit(Result.Loading)
            marketRanksRepository.getRanks().collect {
                emit(Result.Success(it))
            }
        }.catch { e ->
            emit(Result.Error(Exception(e)))
        }.flowOn(coroutineDispatcher)
    }
}