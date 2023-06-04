package com.mrostami.geckoincompose.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mrostami.geckoincompose.model.GlobalMarketInfo
import com.mrostami.geckoincompose.ui.components.PieChart
import com.mrostami.geckoincompose.ui.components.StateView
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme
import timber.log.Timber

@Composable
fun HomeScreen(
    title: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = GeckoinTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = GeckoinTheme.customColors.textPrimary
        )
        Spacer(modifier = Modifier.size(Dp(20f)))
        MarketDominanceWidget()
    }
}


@Composable
fun MarketDominanceWidget(
    viewModel: MarketDominanceViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    var uiState = viewModel.uiState.collectAsState()
    StateView(
        uiModel = uiState.value, 
        retryOnError = {viewModel.onNewEvent(MarketDominanceEvents.RefreshData)}
    ) {
        Timber.e("Collected values: ${uiState.value}")
        DominanceChart(globalMarketInfo = uiState.value.data)
    }
}

@Composable
fun DominanceChart(
    globalMarketInfo: GlobalMarketInfo,
    modifier: Modifier = Modifier
) {
    var caps: MutableMap<String, Double> = mutableMapOf()
    globalMarketInfo.marketCapPercentages?.forEach {
        caps.put(it.coinId, it.cap)
    }
    PieChart(
        data = caps,
        radiusOuter = Dp(85f),
        chartBarWidth = Dp(40f)
        )
}