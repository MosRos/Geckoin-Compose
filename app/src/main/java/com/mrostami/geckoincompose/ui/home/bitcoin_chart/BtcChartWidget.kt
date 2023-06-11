package com.mrostami.geckoincompose.ui.home.bitcoin_chart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.mrostami.geckoincompose.R
import com.mrostami.geckoincompose.model.BitcoinPriceInfo
import com.mrostami.geckoincompose.ui.components.StateView
import com.mrostami.geckoincompose.ui.components.TenDaysLineChart
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme
import com.mrostami.geckoincompose.utils.decimalFormat
import com.mrostami.geckoincompose.utils.round
import timber.log.Timber
import kotlin.math.roundToLong

@Composable
fun BtcChartWidget(
    modifier: Modifier = Modifier,
    viewModel: BtcInfoViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    StateView(
        uiModel = uiState.value,
        retryOnError = { viewModel.onNewEvent(BtcInfoEvents.RefreshData) }
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
                    }
                    .onSizeChanged {
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
                    BtcBasicInfoView(btcPriceInfo = uiState.value.data.btcPriceInfo)
                    TenDaysLineChart(data = uiState.value.data.btcChartInfo)
                }
            }
        }
    }
}

@Composable
fun BtcBasicInfoView(
    btcPriceInfo: BitcoinPriceInfo,
    modifier: Modifier = Modifier
) {
    ConstraintLayout (
        modifier = modifier.fillMaxWidth()
    ) {
        val (image, title, price, vol, cap, change, changIcon) = createRefs()
        AsyncImage(
            model = "https://assets.coingecko.com/coins/images/1/large/bitcoin.png",
            contentDescription = "bitcoin",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(Dp(40f))
                .clip(CircleShape)
                .constrainAs(image) {
                    start.linkTo(parent.start,  Dp(12f))
                    top.linkTo(parent.top, Dp(12f))
                }
        )
        Text(
            text = "Bitcoin",
            style = GeckoinTheme.typography.bodyLarge,
            color = GeckoinTheme.customColors.textPrimary,
            modifier = Modifier.constrainAs(title) {
                start.linkTo(image.end, Dp(8f))
                top.linkTo(parent.top, Dp(8f))
            } 
        )
        Text(
            text = "$ " + (btcPriceInfo.info.usd?.round(decimals = 0) ?: 0),
            style = GeckoinTheme.typography.bodyLarge,
            color = GeckoinTheme.customColors.textPrimary,
            modifier = Modifier.constrainAs(price) {
                start.linkTo(title.end)
                end.linkTo(change.start)
                top.linkTo(title.top)
            }
        )

        Text(
            text = (btcPriceInfo.info.usd24hChange?.round(decimals = 2) ?: 0.0).toString() + "%",
            style = GeckoinTheme.typography.bodySmall,
            color = getUpOrDownColor(btcPriceInfo = btcPriceInfo),
            modifier = Modifier.constrainAs(change) {
                end.linkTo(changIcon.start)
                top.linkTo(price.top)
            }
        )
        getUpOrDownPainterResource(btcPriceInfo = btcPriceInfo)?.let { p ->
            Icon(
                painter = p,
                tint = getUpOrDownColor(btcPriceInfo = btcPriceInfo),
                contentDescription = "percent change",
                modifier = Modifier
                    .size(Dp(24f))
                    .constrainAs(changIcon) {
                        end.linkTo(parent.end, Dp(8f))
                        top.linkTo(change.top)
                    }
            )
        }

        Text(
            text = "vol: " + (btcPriceInfo.info.usd24hVol?.roundToLong()?.decimalFormat() ?: 0.0).toString(),
            style = GeckoinTheme.typography.bodyMedium,
            color = GeckoinTheme.customColors.textSecondary,
            modifier = Modifier.constrainAs(vol) {
                start.linkTo(title.start)
                top.linkTo(title.bottom, Dp(8f))
            }
        )

        Text(
            text = "cap: " + (btcPriceInfo.info.usdMarketCap?.roundToLong()?.decimalFormat() ?: 0.0).toString(),
            style = GeckoinTheme.typography.bodyMedium,
            color = GeckoinTheme.customColors.textSecondary,
            modifier = Modifier.constrainAs(cap) {
                start.linkTo(title.start)
                top.linkTo(vol.bottom)
            }
        )

    }
}

@Composable
fun getUpOrDownColor(btcPriceInfo: BitcoinPriceInfo) : Color {
    if (btcPriceInfo.info.usd24hChange == null) return GeckoinTheme.customColors.dividerColor
    val change: Double = btcPriceInfo.info.usd24hChange ?: 0.0
    return when {
        change == 0.0 -> GeckoinTheme.customColors.dividerColor
        change > 0 -> GeckoinTheme.customColors.upGreen
        change < 0 -> GeckoinTheme.customColors.downRed
        else -> { GeckoinTheme.customColors.dividerColor }
    }
}

@Composable
fun getUpOrDownPainterResource(btcPriceInfo: BitcoinPriceInfo) : Painter? {
    if (btcPriceInfo.info.usd24hChange == null) return null
    val change: Double = btcPriceInfo.info.usd24hChange ?: 0.0
    return when {
        change == 0.0 -> null
        change > 0 -> painterResource(id = R.drawable.ic_arrow_drop_up)
        change < 0 -> painterResource(id = R.drawable.ic_arrow_drop_down)
        else -> null
    }
}