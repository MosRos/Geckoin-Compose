package com.mrostami.geckoincompose.ui.home.bitcoin_chart

import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.domain.base.data
import com.mrostami.geckoincompose.domain.base.succeeded
import com.mrostami.geckoincompose.domain.usecases.BitcoinChartInfoUseCase
import com.mrostami.geckoincompose.domain.usecases.BitcoinSimplePriceUseCase
import com.mrostami.geckoincompose.model.BitcoinPriceInfo
import com.mrostami.geckoincompose.ui.base.BaseUiState
import com.mrostami.geckoincompose.ui.base.StateMachine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

class BtcInfoStateMachine @Inject constructor(
    val initState: BtcInfoUiState,
    val coroutineScope: CoroutineScope,
    val btcChartInfoUseCase: BitcoinChartInfoUseCase,
    val btcPriceUseCase: BitcoinSimplePriceUseCase
) :
    StateMachine<BtcInfoUiState, BtcInfoEvents, BtcInfoEffects>(initialState = initState) {

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