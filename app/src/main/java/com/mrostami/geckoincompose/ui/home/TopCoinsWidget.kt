package com.mrostami.geckoincompose.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.mrostami.geckoincompose.model.GlobalMarketInfo
import com.mrostami.geckoincompose.ui.components.PieChart
import com.mrostami.geckoincompose.ui.components.StateView
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme
import timber.log.Timber


@Composable
fun MarketDominanceWidget(
    modifier: Modifier = Modifier,
    viewModel: MarketDominanceViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    StateView(
        uiModel = uiState.value,
        retryOnError = { viewModel.onNewEvent(MarketDominanceEvents.RefreshData) }
    ) {
        Timber.e("Collected values: ${uiState.value}")
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .padding(Dp(12f))
        ) {
            val (card, surface) = createRefs()
            var heightInDp by remember {
                mutableStateOf(DpSize.Zero)
            }
            val density = LocalDensity.current
            Surface(
                shape = GeckoinTheme.shapes.large,
                tonalElevation = Dp(1f),
                shadowElevation = Dp(0f),
                border = BorderStroke(width = Dp(1f), color = GeckoinTheme.colorScheme.outline),
                modifier = Modifier
                    .constrainAs(surface) {
                        linkTo(start = parent.start, end = parent.end)
                        linkTo(top = parent.top, bottom = parent.bottom)
                        width = Dimension.ratio("4:3")
                        height = Dimension.fillToConstraints
                    }.onSizeChanged {
                        heightInDp = density.run {
                            DpSize(
                                it.width.toDp(),
                                it.height.toDp()
                            )
                        }
                    }
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.background(color = GeckoinTheme.colorScheme.surface)
                ) {
                    DominanceChart(
                        globalMarketInfo = uiState.value.data,
                        boxHeight = heightInDp.height,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun DominanceChart(
    globalMarketInfo: GlobalMarketInfo,
    boxHeight: Dp,
    modifier: Modifier = Modifier
) {
    val caps: MutableMap<String, Double> = mutableMapOf()
    globalMarketInfo.marketCapPercentages?.forEach {
        caps.put(it.coinId, it.cap)
    }
    PieChart(
        data = caps,
        radiusOuter = Dp((boxHeight.value * 0.35).toFloat()),
        chartBarWidth = Dp(33f)
    )
}