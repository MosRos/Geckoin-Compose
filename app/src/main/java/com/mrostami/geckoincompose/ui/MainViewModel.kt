package com.mrostami.geckoincompose.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrostami.geckoincompose.data.remote.RemoteDataSource
import com.mrostami.geckoincompose.domain.usecases.ThemeConfigUseCase
import com.mrostami.geckoincompose.model.ThemeMode
import com.mrostami.geckoincompose.model.toThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val themeConfigUseCase: ThemeConfigUseCase
) : ViewModel() {

    private var _themeMode: MutableStateFlow<ThemeMode> =
        MutableStateFlow(ThemeMode.AUTO)
    val themeMode: StateFlow<ThemeMode> = _themeMode

    init {
        checkApiConnection()
        observeAppThemeState()
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

    private fun observeAppThemeState() {
        viewModelScope.launch(Dispatchers.IO) {
            themeConfigUseCase.getThemeMode().collectLatest {
                Timber.i(it.toString())
                _themeMode.emit(it.toThemeMode())
            }
        }
    }
}