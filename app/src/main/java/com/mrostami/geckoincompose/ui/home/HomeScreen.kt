package com.mrostami.geckoincompose.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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
        retryOnError = { viewModel.onNewEvent(MarketDominanceEvents.RefreshData) }
    ) {
        Timber.e("Collected values: ${uiState.value}")
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dp(12f))
        ) {
            val (card, surface) = createRefs()
            var heightInDp by remember {
                mutableStateOf(DpSize.Zero)
            }
            var density = LocalDensity.current
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
    var caps: MutableMap<String, Double> = mutableMapOf()
    globalMarketInfo.marketCapPercentages?.forEach {
        caps.put(it.coinId, it.cap)
    }
    PieChart(
        data = caps,
        radiusOuter = Dp((boxHeight.value * 0.35).toFloat()),
        chartBarWidth = Dp(35f)
    )
}