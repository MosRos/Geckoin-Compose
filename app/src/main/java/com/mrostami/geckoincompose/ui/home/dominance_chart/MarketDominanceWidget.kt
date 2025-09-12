package com.mrostami.geckoincompose.ui.home.dominance_chart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mrostami.geckoincompose.model.GlobalMarketInfo
import com.mrostami.geckoincompose.ui.components.PieChart
import com.mrostami.geckoincompose.ui.components.StateView
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme
import timber.log.Timber


@Composable
fun MarketDominanceWidget(
    modifier: Modifier = Modifier,
    viewModel: MarketDominanceViewModel = hiltViewModel()
//    stateMachine: MarketDominanceStateMachine
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sendEvent(event = MarketDominanceEvents.RefreshData)
    }

    StateView(
        uiModel = uiState.value,
        retryOnError = { viewModel.sendEvent(MarketDominanceEvents.RefreshData) }
    ) {
        Timber.e("Collected values: ${uiState.value}")
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(5 / 4f)
                .padding(Dp(12f))
        ) {
            val (card, surface) = createRefs()
            Surface(
                shape = GeckoinTheme.shapes.large,
                tonalElevation = Dp(1f),
                shadowElevation = Dp(0f),
                border = BorderStroke(width = Dp(1f), color = GeckoinTheme.colorScheme.outline),
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(surface) {
                        linkTo(start = parent.start, end = parent.end)
                        linkTo(top = parent.top, bottom = parent.bottom)
                    }
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(color = GeckoinTheme.colorScheme.surface)
                        .fillMaxWidth()
                ) {
                    DominanceChart(
                        globalMarketInfo = uiState.value.data,
                        modifier = Modifier
                            .fillMaxSize()
                            .aspectRatio(5 / 4f)
                    )
                }
            }
        }
    }
}

@Composable
fun DominanceChart(
    globalMarketInfo: GlobalMarketInfo,
    modifier: Modifier = Modifier
) {
    val caps: MutableMap<String, Double> = mutableMapOf()
    globalMarketInfo.marketCapPercentages?.forEach {
        caps.put(it.coinId, it.cap)
    }
    PieChart(
        data = caps,
        chartBarWidth = Dp(33f)
    )
}