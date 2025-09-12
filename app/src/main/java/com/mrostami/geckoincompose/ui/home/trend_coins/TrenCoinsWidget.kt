package com.mrostami.geckoincompose.ui.home.trend_coins

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mrostami.geckoincompose.ui.components.StateView
import com.mrostami.geckoincompose.ui.components.TrendCoinItemView
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme
import kotlinx.coroutines.delay

@Composable
fun TrendCoinsWidget(
    modifier: Modifier = Modifier,
    viewModel: TrendCoinsViewModel = hiltViewModel<TrendCoinsViewModel>()
//    stateMachine: TrendCoinsStateMachine
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    var visibleItemCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.sendEvent(TrendCoinsEvents.RefreshData)
    }

    LaunchedEffect(key1 = uiState.value.data, key2 = uiState.value.state) {
        if (uiState.value.data.isNotEmpty()) {
            uiState.value.data.forEachIndexed { index, coin ->
                if (index == 0) {
                    delay(300)
                } else {
                    delay(100)
                }
                visibleItemCount = index
            }
        }
    }

    StateView(
        uiModel = uiState.value,
        retryOnError = { viewModel.sendEvent(TrendCoinsEvents.RefreshData) }
    ) {
        Surface(
            shape = GeckoinTheme.shapes.large,
            tonalElevation = Dp(1f),
            shadowElevation = Dp(0f),
            border = BorderStroke(width = Dp(1f), color = GeckoinTheme.colorScheme.outline),
            modifier = Modifier
                .padding(Dp(12f))
        ) {
//            LazyColumn(state = listState) {
//                items(
//                    items = uiState.value.data,
//                    key = { item -> item.coinId }
//                ) { coin ->
//                    TrendCoinItemView(coin = coin)
//                }
//            }
            Column(modifier = Modifier.background(color = GeckoinTheme.colorScheme.surface)) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    text = "Trending Coins",
                    style = GeckoinTheme.typography.headlineSmall,
                    color = GeckoinTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )
                uiState.value.data.take(visibleItemCount).forEach { trendCoin ->
                    TrendCoinItemView(coin = trendCoin)
                }
            }
        }
    }
}