package com.mrostami.geckoincompose.ui.home.trend_coins

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mrostami.geckoincompose.ui.components.StateView
import com.mrostami.geckoincompose.ui.components.TrendCoinItemView
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme
import kotlinx.coroutines.delay

@Composable
fun TrendCoinsWidget(
    modifier: Modifier = Modifier,
    stateMachine: TrendCoinsStateMachine
) {

    val uiState = stateMachine.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        delay(300)
        stateMachine.sendEvent(TrendCoinsEvents.RefreshData)
    }

    StateView(
        uiModel = uiState.value,
        retryOnError = { stateMachine.sendEvent(TrendCoinsEvents.RefreshData) }
    ) {
        Surface(
            shape = GeckoinTheme.shapes.large,
            tonalElevation = Dp(1f),
            shadowElevation = Dp(0f),
            border = BorderStroke(width = Dp(1f), color = GeckoinTheme.colorScheme.outline),
            modifier = Modifier
//                .background(color = GeckoinTheme.colorScheme.surface)
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
                uiState.value.data.forEach { trendCoin ->  
                    TrendCoinItemView(coin = trendCoin)
                }
            }
        }
    }
}