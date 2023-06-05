package com.mrostami.geckoincompose.ui.home.tend_coins

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mrostami.geckoincompose.model.TrendCoin
import com.mrostami.geckoincompose.ui.components.StateView
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme

@Composable
fun TrendCoinsWidget(
    modifier: Modifier = Modifier,
    viewModel: TrendCoinsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    StateView(
        uiModel = uiState.value,
        retryOnError = { viewModel.onNewEvent(TrendCoinsEvents.RefreshData) }
    ) {
        Surface(
            shape = GeckoinTheme.shapes.large,
            tonalElevation = Dp(1f),
            shadowElevation = Dp(0f),
            border = BorderStroke(width = Dp(1f), color = GeckoinTheme.colorScheme.outline),
            modifier = Modifier.padding(Dp(12f))
        ) {
//            LazyColumn(state = listState) {
//                items(
//                    items = uiState.value.data,
//                    key = { item -> item.coinId }
//                ) { coin ->
//                    TrendCoinItemView(coin = coin)
//                }
//            }
            Column {
                uiState.value.data.forEach { trendCoin ->  
                    TrendCoinItemView(coin = trendCoin)
                }
            }
        }
    }
}

@Composable
fun TrendCoinItemView(
    coin: TrendCoin,
    modifier: Modifier = Modifier
) {
    ConstraintLayout {
        val (image, symbol, name, rank, divider) = createRefs()

        AsyncImage(
            model = coin.small,
            contentDescription = "bitcoin",
            modifier = Modifier
                .size(Dp(40f))
                .constrainAs(image) {
                    start.linkTo(parent.start, Dp(8f))
                    top.linkTo(parent.top, Dp(8f))
                    bottom.linkTo(parent.bottom, Dp(8f))

                }
        )
        Text(
            text = coin.symbol?.uppercase() ?: "",
            style = GeckoinTheme.typography.bodyLarge,
            color = GeckoinTheme.customColors.textPrimary,
            modifier = Modifier.constrainAs(symbol) {
                start.linkTo(image.end, Dp(10f))
                top.linkTo(parent.top, Dp(8f))
            }
        )
        Text(
            text = coin.name ?: "",
            style = GeckoinTheme.typography.bodyMedium,
            color = GeckoinTheme.customColors.textSecondary,
            modifier = Modifier.constrainAs(name) {
                start.linkTo(symbol.start)
                top.linkTo(symbol.bottom, Dp(5f))
                bottom.linkTo(parent.bottom, Dp(8f))
            }
        )

        Text(
            text = "#" + coin.marketCapRank.toString(),
            style = GeckoinTheme.typography.titleMedium,
            color = GeckoinTheme.customColors.textPrimary,
            modifier = Modifier.constrainAs(rank) {
                end.linkTo(parent.end, Dp(10f))
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        )

        Divider(
            thickness = Dp(1f),
            color = GeckoinTheme.customColors.dividerColor,
            modifier = Modifier.constrainAs(divider){
                start.linkTo(name.start)
                end.linkTo(rank.start, Dp(8f))
                bottom.linkTo(parent.bottom)
            }
        )
    }
}