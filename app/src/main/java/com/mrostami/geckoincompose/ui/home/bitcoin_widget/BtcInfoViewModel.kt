package com.mrostami.geckoincompose.ui.home.bitcoin_widget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrostami.geckoincompose.domain.base.Result
import com.mrostami.geckoincompose.domain.base.data
import com.mrostami.geckoincompose.domain.base.succeeded
import com.mrostami.geckoincompose.domain.usecases.BitcoinChartInfoUseCase
import com.mrostami.geckoincompose.domain.usecases.BitcoinSimplePriceUseCase
import com.mrostami.geckoincompose.model.BitcoinPriceInfo
import com.mrostami.geckoincompose.model.PriceEntry
import com.mrostami.geckoincompose.ui.base.BaseUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class BtcInfoViewModel @Inject constructor(
    private val btcChartInfoUseCase: BitcoinChartInfoUseCase,
    private val btcPriceUseCase: BitcoinSimplePriceUseCase
) : ViewModel() {

    private var _uiState: MutableStateFlow<BtcInfoUiModel> = MutableStateFlow(BtcInfoUiModel.defaultInitState)
    val uiState: StateFlow<BtcInfoUiModel> = _uiState

    var uiEffects: Channel<BtcInfoEffects> = Channel()
        private set

    init {
        getBtcMarketInfo()
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
                    BtcInfoUiModel(
                        state = BaseUiModel.State.SUCCESS,
                        errorMessage = null,
                        data = BtcUiInfo(
                            btcPriceInfo = priceInfo.data,
                            btcChartInfo = chartInfo.data
                        )
                    )
                } else if (priceInfo is Result.Error || chartInfo is Result.Error) {
                    val errorMessage = (priceInfo as? Result.Error)?.message
                        ?: (chartInfo as? Result.Error)?.message ?: "an error occured"
                    BtcInfoUiModel(
                        state = BaseUiModel.State.ERROR,
                        errorMessage = errorMessage,
                        data = BtcUiInfo(
                            btcPriceInfo = BitcoinPriceInfo(),
                            btcChartInfo = listOf()
                        )
                    )
                } else {
                    BtcInfoUiModel.defaultInitState
                }

            }.collectLatest { result ->
                _uiState.emit(result)
            }

        }
    }

    fun onNewEvent(events: BtcInfoEvents) {
        reduce(events = events, oldState = _uiState.value)
    }

    private fun reduce(events: BtcInfoEvents, oldState: BtcInfoUiModel) {
        when(events) {
            is BtcInfoEvents.RefreshData -> {
                getBtcMarketInfo()
            }
        }
    }
}

data class BtcUiInfo(
    val btcPriceInfo: BitcoinPriceInfo,
    val btcChartInfo: List<PriceEntry>
)
@Immutable
data class BtcInfoUiModel(
    override val state: BaseUiModel.State,
    override val errorMessage: String?,
    override val data: BtcUiInfo
) : BaseUiModel {
    companion object {
        val defaultInitState = BtcInfoUiModel(
            state = BaseUiModel.State.LOADING,
            errorMessage = null,
            data = BtcUiInfo(
                btcPriceInfo = BitcoinPriceInfo(),
                btcChartInfo = listOf()
            )
        )
    }
}

sealed interface BtcInfoEffects {
    object NoEffect : BtcInfoEffects
}

sealed interface BtcInfoEvents {
    object RefreshData : BtcInfoEvents
}