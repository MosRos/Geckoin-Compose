package com.mrostami.geckoincompose.ui.home.dominance_chart

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

    val marketDominanceStateMachine: MarketDominanceStateMachine by lazy {
        MarketDominanceStateMachine(
            globalMarketInfoUseCase = globalMarketInfoUseCase,
            coroutineScope = viewModelScope
        )
    }
}