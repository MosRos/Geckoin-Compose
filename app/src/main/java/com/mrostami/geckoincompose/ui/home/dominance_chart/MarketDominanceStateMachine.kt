package com.mrostami.geckoincompose.ui.home.dominance_chart

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.domain.usecases.GlobalMarketInfoUseCase
import com.mrostami.geckoincompose.model.GlobalMarketInfo
import com.mrostami.geckoincompose.ui.base.BaseUiEffect
import com.mrostami.geckoincompose.ui.base.BaseUiEvent
import com.mrostami.geckoincompose.ui.base.BaseUiState
import com.mrostami.geckoincompose.ui.base.StateMachine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MarketDominanceStateMachine @Inject constructor(
    val globalMarketInfoUseCase: GlobalMarketInfoUseCase,
    val coroutineScope: CoroutineScope
) : StateMachine<DominanceUiState, MarketDominanceEvents, MarketDominanceEffects>(initialState = DominanceUiState.defaultInitState){


    override fun reduce(event: MarketDominanceEvents, oldState: DominanceUiState) {
        when(event) {
            is MarketDominanceEvents.RefreshData -> {
                getMarketDominanceInfo()
            }
        }
    }

    private fun getMarketDominanceInfo() {
        coroutineScope.launch(Dispatchers.IO) {
            globalMarketInfoUseCase.invoke(forceRefresh = true).collectLatest { result ->
                when(result) {
                    is Result.Success ->  {
                        Timber.e(result.data.toString())
                        updateState(
                            DominanceUiState(
                                data = result.data,
                                state = BaseUiState.State.SUCCESS,
                                errorMessage = null
                            )
                        )
                    }
                    is Result.Error -> {
                        Timber.e(result.exception.toString())
                        updateState(
                            DominanceUiState(
                                state = BaseUiState.State.ERROR,
                                errorMessage = result.message ?: "error"
                            )
                        )
                    }
                    is Result.Loading -> {
                        Timber.e(result.toString())
                        updateState(
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
}


sealed interface MarketDominanceEvents : BaseUiEvent {
    object RefreshData : MarketDominanceEvents
}

sealed interface MarketDominanceEffects : BaseUiEffect {
    object NoEffect : MarketDominanceEffects
}

@Immutable data class DominanceUiState(
    override val state: BaseUiState.State,
    override val errorMessage: String?,
    override val data: GlobalMarketInfo = GlobalMarketInfo()
) : BaseUiState {
    companion object {
        val defaultInitState = DominanceUiState(
            state = BaseUiState.State.SUCCESS,
            errorMessage = null
        )
    }
}