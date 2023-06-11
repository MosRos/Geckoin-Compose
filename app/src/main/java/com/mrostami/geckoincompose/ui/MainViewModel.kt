package com.mrostami.geckoincompose.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrostami.geckoincompose.data.remote.RemoteDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : ViewModel() {

    init {
        checkApiConnection()
    }

    fun checkApiConnection() {
        viewModelScope.launch(Dispatchers.IO) {
            remoteDataSource.checkCoinGeckoConnection().fold(
                {
                    Timber.e(it.error.toString())
                },
                {
                    Timber.i(it.toString())
                }
            )
        }
    }
}