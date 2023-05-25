/*
 * *
 *  * Created by Moslem Rostami on 7/6/20 6:46 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 7/6/20 6:46 PM
 *
 */

package com.mrostami.geckoincompose.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.mrostami.geckoincompose.domain.usecases.SyncAllCoinsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext


@HiltWorker
class SyncCoinsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val syncAllCoinsUseCase: SyncAllCoinsUseCase
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        try {
            var workResult: ListenableWorker.Result = Result.failure()
            syncAllCoinsUseCase.invoke(forceRefresh = false).collectLatest { result ->
                when (result) {
                    is com.mrostami.geckoincompose.domain.base.Result.Success -> {
                        workResult = Result.success()
                    }
//                    is com.mrostami.geckoincompose.domain.base.Result.Error -> {
//                        workResult = Result.failure()
//                    }
                    else -> workResult = Result.failure()
                }
            }
            workResult
        } catch (e: Exception) {
            Result.failure()
        }
    }
}