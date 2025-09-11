package com.mrostami.geckoincompose.ui.home.trend_coins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrostami.geckoincompose.domain.usecases.TrendCoinsUseCase
import com.mrostami.geckoincompose.model.TrendCoin
import com.mrostami.geckoincompose.ui.base.BaseUiEffect
import com.mrostami.geckoincompose.ui.base.BaseUiEvent
import com.mrostami.geckoincompose.ui.base.BaseUiState
import com.mrostami.geckoincompose.ui.base.StateMachine
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.annotation.concurrent.Immutable
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
}

