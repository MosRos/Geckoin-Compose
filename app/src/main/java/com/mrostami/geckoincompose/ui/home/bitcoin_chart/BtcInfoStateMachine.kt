package com.mrostami.geckoincompose.ui.home.bitcoin_chart

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
import com.mrostami.geckoincompose.ui.base.StateMachine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

class BtcInfoStateMachine @Inject constructor(
    val initState: BtcInfoUiState,
    val coroutineScope: CoroutineScope,
    val btcChartInfoUseCase: BitcoinChartInfoUseCase,
    val btcPriceUseCase: BitcoinSimplePriceUseCase
) : StateMachine<BtcInfoUiState, BtcInfoEvents, BtcInfoEffects>(initialState = initState) {

    override fun reduce(event: BtcInfoEvents, oldState: BtcInfoUiState) {
        when (event) {
            is BtcInfoEvents.RefreshData -> {
                getBtcMarketInfo()
            }
        }
    }

    private fun getBtcMarketInfo() {
        coroutineScope.launch(Dispatchers.IO) {
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
                updateState(result)
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