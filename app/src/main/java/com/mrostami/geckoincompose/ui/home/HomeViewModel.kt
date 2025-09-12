package com.mrostami.geckoincompose.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrostami.geckoincompose.domain.usecases.BitcoinChartInfoUseCase
import com.mrostami.geckoincompose.domain.usecases.BitcoinSimplePriceUseCase
import com.mrostami.geckoincompose.domain.usecases.GlobalMarketInfoUseCase
import com.mrostami.geckoincompose.domain.usecases.TrendCoinsUseCase
import com.mrostami.geckoincompose.ui.home.bitcoin_chart.BtcInfoStateMachine
import com.mrostami.geckoincompose.ui.home.bitcoin_chart.BtcInfoUiState
import com.mrostami.geckoincompose.ui.home.dominance_chart.MarketDominanceStateMachine
import com.mrostami.geckoincompose.ui.home.trend_coins.TrendCoinsStateMachine
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val globalMarketInfoUseCase: GlobalMarketInfoUseCase,
    private val trendCoinsUseCase: TrendCoinsUseCase,
    private val bitcoinSimplePriceUseCase: BitcoinSimplePriceUseCase,
    private val bitcoinChartInfoUseCase: BitcoinChartInfoUseCase
) : ViewModel() {

    val btcInfoStateMachine by  lazy {
        BtcInfoStateMachine(
            initState = BtcInfoUiState.defaultInitState,
            coroutineScope = viewModelScope,
            btcChartInfoUseCase = bitcoinChartInfoUseCase,
            btcPriceUseCase = bitcoinSimplePriceUseCase
        )
    }


    val marketDominanceStateMachine: MarketDominanceStateMachine by lazy {
        MarketDominanceStateMachine(
            globalMarketInfoUseCase = globalMarketInfoUseCase,
            coroutineScope = viewModelScope
        )
    }

    val trendCoinsStateMachine: TrendCoinsStateMachine by lazy {
        TrendCoinsStateMachine(
            trendCoinsUseCase = trendCoinsUseCase,
            scope = viewModelScope
        )
    }

}