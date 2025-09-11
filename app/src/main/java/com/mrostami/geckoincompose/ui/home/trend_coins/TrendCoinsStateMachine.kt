package com.mrostami.geckoincompose.ui.home.trend_coins

import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.domain.usecases.TrendCoinsUseCase
import com.mrostami.geckoincompose.model.TrendCoin
import com.mrostami.geckoincompose.ui.base.BaseUiEffect
import com.mrostami.geckoincompose.ui.base.BaseUiEvent
import com.mrostami.geckoincompose.ui.base.BaseUiState
import com.mrostami.geckoincompose.ui.base.StateMachine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

class TrendCoinsStateMachine @Inject constructor(
    val trendCoinsUseCase: TrendCoinsUseCase,
    val scope: CoroutineScope
) : StateMachine<TrendCoinsUiState, TrendCoinsEvents, TrendCoinsEffects>(
    initialState = TrendCoinsUiState.defaultInitiState
) {

    override fun reduce(event: TrendCoinsEvents, oldState: TrendCoinsUiState) {
        when (event) {
            is TrendCoinsEvents.RefreshData -> getTrendCoins()
        }
    }

    private fun getTrendCoins() {
        scope.launch(Dispatchers.IO) {
            trendCoinsUseCase.invoke(forceRefresh = false).collectLatest { result ->
                when (result) {
                    is com.mrostami.geckoincompose.domain.base.Result.Success -> {
                        updateState(
                            state.value.copy(
                                state = BaseUiState.State.SUCCESS,
                                errorMessage = null,
                                data = result.data
                            )
                        )
                    }

                    is com.mrostami.geckoincompose.domain.base.Result.Error -> {
                        updateState(
                            state.value.copy(
                                state = BaseUiState.State.ERROR,
                                errorMessage = result.message ?: "Error",
                                data = listOf()
                            )
                        )
                    }

                    is Result.Loading -> {
                        updateState(state.value.copy(state = BaseUiState.State.LOADING))
                    }
                }
            }
        }
    }
}

sealed interface TrendCoinsEffects : BaseUiEffect {
    object NoEffect : TrendCoinsEffects
}

sealed interface TrendCoinsEvents : BaseUiEvent {
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