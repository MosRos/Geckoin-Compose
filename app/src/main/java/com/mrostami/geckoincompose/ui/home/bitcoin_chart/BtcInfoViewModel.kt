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

    private val btcInfoStateMachine = BtcInfoStateMachine(
        initState = BtcInfoUiState.defaultInitState,
        coroutineScope = viewModelScope,
        btcChartInfoUseCase = btcChartInfoUseCase,
        btcPriceUseCase = btcPriceUseCase
    )

    val uiState: StateFlow<BtcInfoUiState>
        get() = btcInfoStateMachine.state

    val uiEffects: Flow<BtcInfoEffects>
        get() = btcInfoStateMachine.effect

    init {
        btcInfoStateMachine.sendEvent(event = BtcInfoEvents.RefreshData)
    }

    fun onNewEvent(events: BtcInfoEvents) {
        btcInfoStateMachine.sendEvent(events)
    }

}

data class BtcUiInfo(
    val btcPriceInfo: BitcoinPriceInfo,
    val btcChartInfo: List<PriceEntry>
)
@Immutable
data class BtcInfoUiState(
    override val state: BaseUiState.State,
    override val errorMessage: String?,
    override val data: BtcUiInfo
) : BaseUiState {
    companion object {
        val defaultInitState = BtcInfoUiState(
            state = BaseUiState.State.LOADING,
            errorMessage = null,
            data = BtcUiInfo(
                btcPriceInfo = BitcoinPriceInfo(),
                btcChartInfo = listOf()
            )
        )
    }
}

sealed interface BtcInfoEffects : BaseUiEffect {
    object NoEffect : BtcInfoEffects
}

sealed interface BtcInfoEvents : BaseUiEvent {
    object RefreshData : BtcInfoEvents
}