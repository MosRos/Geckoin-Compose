package com.mrostami.geckoincompose.ui.home.trend_coins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrostami.geckoincompose.domain.usecases.TrendCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TrendCoinsViewModel @Inject constructor(
    private val trendCoinsUseCase: TrendCoinsUseCase
) : ViewModel() {
    val trendCoinsStateMachine: TrendCoinsStateMachine by lazy {
        TrendCoinsStateMachine(
            trendCoinsUseCase = trendCoinsUseCase,
            scope = viewModelScope
        )
    }


    val uiState: StateFlow<TrendCoinsUiState>
        get() = trendCoinsStateMachine.state

    val uiEffects: Flow<TrendCoinsEffects>
        get() = trendCoinsStateMachine.effects

    init {
//        trendCoinsStateMachine.sendEvent(event = TrendCoinsEvents.RefreshData)
    }

    fun sendEvent(event: TrendCoinsEvents) {
        trendCoinsStateMachine.sendEvent(event)
    }

}

