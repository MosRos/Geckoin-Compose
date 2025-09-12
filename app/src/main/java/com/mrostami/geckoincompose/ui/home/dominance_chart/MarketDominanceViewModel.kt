package com.mrostami.geckoincompose.ui.home.dominance_chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.domain.usecases.GlobalMarketInfoUseCase
import com.mrostami.geckoincompose.ui.base.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MarketDominanceViewModel @Inject constructor(
    private val globalMarketInfoUseCase: GlobalMarketInfoUseCase
) : ViewModel() {

//    val marketDominanceStateMachine: MarketDominanceStateMachine =
//        MarketDominanceStateMachine(
//            globalMarketInfoUseCase = globalMarketInfoUseCase,
//            coroutineScope = viewModelScope
//        )


    var uiState: MutableStateFlow<DominanceUiState> =
        MutableStateFlow<DominanceUiState>(DominanceUiState.defaultInitState)
        private set
//        get() = marketDominanceStateMachine.state

    var uiEffects: MutableSharedFlow<MarketDominanceEffects> =
        MutableSharedFlow<MarketDominanceEffects>()
        private set
//        get() = marketDominanceStateMachine.effects

    init {
//        marketDominanceStateMachine.sendEvent(event = MarketDominanceEvents.RefreshData)
    }

    fun sendEvent(event: MarketDominanceEvents) {
        reduce(event = event, oldState = uiState.value)
    }

    fun reduce(event: MarketDominanceEvents, oldState: DominanceUiState) {
        when (event) {
            is MarketDominanceEvents.RefreshData -> {
                getMarketDominanceInfo()
            }
        }
    }

    private fun getMarketDominanceInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            globalMarketInfoUseCase.invoke(forceRefresh = true).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        Timber.e(result.data.toString())
                        uiState.value = DominanceUiState(
                            data = result.data,
                            state = BaseUiState.State.SUCCESS,
                            errorMessage = null
                        )
                    }

                    is Result.Error -> {
                        Timber.e(result.exception.toString())
                        uiState.value =
                            DominanceUiState(
                                state = BaseUiState.State.ERROR,
                                errorMessage = result.message ?: "error"
                            )
                    }

                    is Result.Loading -> {
                        Timber.e(result.toString())
                        uiState.value =
                            DominanceUiState(
                                state = BaseUiState.State.LOADING,
                                errorMessage = null
                            )
                    }
                }
            }
        }
    }

}