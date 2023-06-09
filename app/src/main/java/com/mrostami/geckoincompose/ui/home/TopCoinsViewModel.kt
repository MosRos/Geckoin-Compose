package com.mrostami.geckoincompose.ui.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.domain.usecases.GlobalMarketInfoUseCase
import com.mrostami.geckoincompose.model.GlobalMarketInfo
import com.mrostami.geckoincompose.ui.base.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MarketDominanceViewModel @Inject constructor(
    private val globalMarketInfoUseCase: GlobalMarketInfoUseCase
) : ViewModel() {

    private var _uiState: MutableStateFlow<DominanceUiState> = MutableStateFlow(DominanceUiState.defaultInitState)
    val uiState: StateFlow<DominanceUiState> = _uiState

    var uiEffects: Channel<MarketDominanceEffects> = Channel()
        private set

    init {
        getMarketDominanceInfo()
    }
    private fun getMarketDominanceInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            globalMarketInfoUseCase.invoke(forceRefresh = true).collectLatest { result ->
                when(result) {
                    is Result.Success ->  {
                        Timber.e(result.data.toString())
                        _uiState.emit(
                            DominanceUiState(
                            data = result.data,
                            state = BaseUiState.State.SUCCESS,
                            errorMessage = null
                            )
                        )
                    }
                    is Result.Error -> {
                        Timber.e(result.exception.toString())
                        _uiState.emit(
                            DominanceUiState(
                            state = BaseUiState.State.ERROR,
                            errorMessage = (result as Result.Error).exception.message ?: "error"
                            )
                        )
                    }
                    is Result.Loading -> {
                        Timber.e(result.toString())
                        _uiState.emit(
                            DominanceUiState(
                                state = BaseUiState.State.LOADING,
                                errorMessage = null
                            )
                        )
                    }
                }
            }
        }
    }

    fun onNewEvent(event: MarketDominanceEvents) {
        reduce(event = event, oldState = uiState.value)
    }
    private fun reduce(event: MarketDominanceEvents, oldState: BaseUiState)  {
        return when(event) {
            is MarketDominanceEvents.RefreshData -> {
                getMarketDominanceInfo()
            }
        }
    }
}


sealed interface MarketDominanceEvents {
    object RefreshData : MarketDominanceEvents
}

sealed interface MarketDominanceEffects {
    object NoEffect : MarketDominanceEffects
}

@Immutable data class DominanceUiState(
    override val state: BaseUiState.State,
    override val errorMessage: String?,
    override val data: GlobalMarketInfo = GlobalMarketInfo()
) : BaseUiState {
    companion object {
        val defaultInitState = DominanceUiState(
            state = BaseUiState.State.LOADING,
            errorMessage = null
        )
    }
}