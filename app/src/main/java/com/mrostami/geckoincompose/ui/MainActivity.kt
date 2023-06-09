package com.mrostami.geckoincompose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.mrostami.geckoincompose.workers.SyncCoinsWorker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels<MainViewModel>()
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.checkApiConnection()
        setContent {
            GeckoinApp()
        }
    }

    override fun onStart() {
        super.onStart()
        initSyncWorker()
    }

    private fun initSyncWorker() {
        val syncWorkConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiredNetworkType(NetworkType.NOT_ROAMING)
            .setRequiresBatteryNotLow(true)
            .build()
        val syncRequest = OneTimeWorkRequestBuilder<SyncCoinsWorker>()
            .setConstraints(syncWorkConstraints)
            .build()
        WorkManager.getInstance(this).enqueue(syncRequest)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GeckoinApp()
}