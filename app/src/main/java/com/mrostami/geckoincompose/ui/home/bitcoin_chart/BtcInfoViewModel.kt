package com.mrostami.geckoincompose.ui.home.bitcoin_chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.domain.base.data
import com.mrostami.geckoincompose.domain.base.succeeded
import com.mrostami.geckoincompose.domain.usecases.BitcoinChartInfoUseCase
import com.mrostami.geckoincompose.domain.usecases.BitcoinSimplePriceUseCase
import com.mrostami.geckoincompose.model.BitcoinPriceInfo
import com.mrostami.geckoincompose.ui.base.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BtcInfoViewModel @Inject constructor(
    private val btcChartInfoUseCase: BitcoinChartInfoUseCase,
    private val btcPriceUseCase: BitcoinSimplePriceUseCase,
) : ViewModel() {

    var uiState: MutableStateFlow<BtcInfoUiState> =
        MutableStateFlow<BtcInfoUiState>(BtcInfoUiState.defaultInitState)
        private set
//        get() = btcInfoStateMachine.state

    var uiEffects: MutableSharedFlow<BtcInfoEffects> = MutableSharedFlow()
        private set
//        get() = btcInfoStateMachine.effects

    init {
//        btcInfoStateMachine.sendEvent(event = BtcInfoEvents.RefreshData)
    }

    fun sendEvent(event: BtcInfoEvents) {
        reduce(event = event, oldState = uiState.value)
    }

    fun reduce(event: BtcInfoEvents, oldState: BtcInfoUiState) {
        when (event) {
            is BtcInfoEvents.RefreshData -> {
                getBtcMarketInfo()
            }
        }
    }

    private fun getBtcMarketInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val priceJob = async {
                btcPriceUseCase.invoke(forceRefresh = false)
            }
            val chartJob = async {
                btcChartInfoUseCase.invoke(forceRefresh = false)
            }

            priceJob.await().combine(chartJob.await()) { priceInfo, chartInfo ->
                if (priceInfo.succeeded && chartInfo.succeeded) {
                    BtcInfoUiState(
                        state = BaseUiState.State.SUCCESS,
                        errorMessage = null,
                        data = BtcUiInfo(
                            btcPriceInfo = priceInfo.data,
                            btcChartInfo = chartInfo.data
                        )
                    )
                } else if (priceInfo is com.mrostami.geckoincompose.domain.base.Result.Error || chartInfo is com.mrostami.geckoincompose.domain.base.Result.Error) {
                    val errorMessage =
                        (priceInfo as? com.mrostami.geckoincompose.domain.base.Result.Error)?.message
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
                uiState.value = result
            }

        }
    }

}