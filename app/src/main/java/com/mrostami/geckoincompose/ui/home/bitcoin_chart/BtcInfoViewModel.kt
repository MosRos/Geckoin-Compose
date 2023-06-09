package com.mrostami.geckoincompose.ui.home.bitcoin_chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.domain.base.data
import com.mrostami.geckoincompose.domain.base.succeeded
import com.mrostami.geckoincompose.domain.usecases.BitcoinChartInfoUseCase
import com.mrostami.geckoincompose.domain.usecases.BitcoinSimplePriceUseCase
import com.mrostami.geckoincompose.model.BitcoinPriceInfo
import com.mrostami.geckoincompose.model.PriceEntry
import com.mrostami.geckoincompose.ui.base.BaseUiEffect
import com.mrostami.geckoincompose.ui.base.BaseUiEvent
import com.mrostami.geckoincompose.ui.base.BaseUiState
import com.mrostami.geckoincompose.ui.base.Reducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class BtcInfoViewModel @Inject constructor(
    private val btcChartInfoUseCase: BitcoinChartInfoUseCase,
    private val btcPriceUseCase: BitcoinSimplePriceUseCase
) : ViewModel() {

    private val reducer = BtcInfoReducer(initState = BtcInfoUiState.defaultInitState)

    val uiState: StateFlow<BtcInfoUiState>
        get() = reducer.state

    val uiEffects: Flow<BtcInfoEffects>
        get() = reducer.effect.receiveAsFlow()

    init {
        reducer.sendEvent(event = BtcInfoEvents.RefreshData)
    }

    fun onNewEvent(events: BtcInfoEvents) {
        reducer.sendEvent(events)
    }

    private inner class BtcInfoReducer(initState: BtcInfoUiState) :
        Reducer<BtcInfoUiState, BtcInfoEvents, BtcInfoEffects>(initialState = initState) {

        override fun reduce(event: BtcInfoEvents, oldState: BtcInfoUiState) {
            when(event) {
                is BtcInfoEvents.RefreshData -> {
                    getBtcMarketInfo()
                }
            }
        }

        private fun getBtcMarketInfo() {
            viewModelScope.launch(Dispatchers.IO) {
                val priceJob = async {
                    btcPriceUseCase.invoke(forceRefresh = false)
                }.await()
                val chartJob = async {
                    btcChartInfoUseCase.invoke(forceRefresh = false)
                }.await()

                priceJob.combine(chartJob) { priceInfo, chartInfo ->
                    if (priceInfo.succeeded && chartInfo.succeeded) {
                        BtcInfoUiState(
                            state = BaseUiState.State.SUCCESS,
                            errorMessage = null,
                            data = BtcUiInfo(
                                btcPriceInfo = priceInfo.data,
                                btcChartInfo = chartInfo.data
                            )
                        )
                    } else if (priceInfo is Result.Error || chartInfo is Result.Error) {
                        val errorMessage = (priceInfo as? Result.Error)?.message
                            ?: (chartInfo as? Result.Error)?.message ?: "an error occured"
                        BtcInfoUiState(
                            state = BaseUiState.State.ERROR,
                            errorMessage = errorMessage,
                            data = BtcUiInfo(
                                btcPriceInfo = BitcoinPriceInfo(),
                                btcChartInfo = listOf()
                            )
                        )
                    } else {
                        BtcInfoUiState.defaultInitState
                    }

                }.collectLatest { result ->
                    setState(result)
                }

            }
        }
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