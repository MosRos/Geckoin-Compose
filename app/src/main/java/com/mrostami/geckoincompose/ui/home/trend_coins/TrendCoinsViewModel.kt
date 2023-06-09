package com.mrostami.geckoincompose.ui.home.trend_coins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.domain.usecases.TrendCoinsUseCase
import com.mrostami.geckoincompose.model.TrendCoin
import com.mrostami.geckoincompose.ui.base.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class TrendCoinsViewModel @Inject constructor(
    private val trendCoinsUseCase: TrendCoinsUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<TrendCoinsUiState> = MutableStateFlow(TrendCoinsUiState.defaultInitiState)
    val uiState: StateFlow<TrendCoinsUiState> = _uiState

    init {
        getTrendCoins()
    }

    private fun getTrendCoins() {
        viewModelScope.launch(Dispatchers.IO) {
            trendCoinsUseCase.invoke(forceRefresh = false).collectLatest { result ->
                when(result) {
                    is Result.Success -> {
                        _uiState.emit(
                            TrendCoinsUiState(
                                state = BaseUiState.State.SUCCESS,
                                errorMessage = null,
                                data = result.data
                            )
                        )
                    }
                    is Result.Error -> {
                        _uiState.emit(
                            TrendCoinsUiState(
                                state = BaseUiState.State.ERROR,
                                errorMessage = result.message ?: "Error",
                                data = listOf()
                            )
                        )
                    }
                    is Result.Loading -> {
                        _uiState.emit(TrendCoinsUiState.defaultInitiState)
                    }
                }
            }
        }
    }

    fun onNewEvent(event: TrendCoinsEvents) {
        reduce(event = event, oldState = _uiState.value)
    }

    private fun reduce(event: TrendCoinsEvents, oldState: TrendCoinsUiState) {
        when(event) {
            is TrendCoinsEvents.RefreshData -> getTrendCoins()
        }
    }

}

sealed interface TrendCoinsEffects {
    object NoEffect : TrendCoinsEffects
}

sealed interface TrendCoinsEvents {
    object RefreshData : TrendCoinsEvents
}

@Immutable
data class TrendCoinsUiState(
    override val state: BaseUiState.State = BaseUiState.State.LOADING,
    override val errorMessage: String? = null,
    override val data: List<TrendCoin> = listOf()
    ) : BaseUiState {
        companion object {
            val defaultInitiState = TrendCoinsUiState()
        }
    }

