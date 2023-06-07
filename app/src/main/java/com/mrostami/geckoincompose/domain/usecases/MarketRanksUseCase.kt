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
import com.mrostami.geckoincompose.model.RankedCoin
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MarketRanksUseCase @Inject constructor(
    private val marketRanksRepository: MarketRanksRepository
) {
    operator fun invoke(): Flow<PagingData<RankedCoin>> = marketRanksRepository.getRanks()
}