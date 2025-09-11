package com.mrostami.geckoincompose.ui.home.bitcoin_chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrostami.geckoincompose.domain.usecases.BitcoinChartInfoUseCase
import com.mrostami.geckoincompose.domain.usecases.BitcoinSimplePriceUseCase
import com.mrostami.geckoincompose.model.BitcoinPriceInfo
import com.mrostami.geckoincompose.model.PriceEntry
import com.mrostami.geckoincompose.ui.base.BaseUiEffect
import com.mrostami.geckoincompose.ui.base.BaseUiEvent
import com.mrostami.geckoincompose.ui.base.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class BtcInfoViewModel @Inject constructor(
    private val btcChartInfoUseCase: BitcoinChartInfoUseCase,
    private val btcPriceUseCase: BitcoinSimplePriceUseCase,
) : ViewModel() {

    val btcInfoStateMachine by lazy {
        BtcInfoStateMachine(
            initState = BtcInfoUiState.defaultInitState,
            coroutineScope = viewModelScope,
            btcChartInfoUseCase = btcChartInfoUseCase,
            btcPriceUseCase = btcPriceUseCase
        )
    }

//    val uiState: StateFlow<BtcInfoUiState>
//        get() = btcInfoStateMachine.state
//
//    val uiEffects: Flow<BtcInfoEffects>
//        get() = btcInfoStateMachine.effects

    init {
//        btcInfoStateMachine.sendEvent(event = BtcInfoEvents.RefreshData)
    }

//    fun onNewEvent(events: BtcInfoEvents) {
//        btcInfoStateMachine.sendEvent(events)
//    }

}