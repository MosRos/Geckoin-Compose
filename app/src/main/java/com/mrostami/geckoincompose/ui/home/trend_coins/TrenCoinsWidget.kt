package com.mrostami.geckoincompose.ui.home.trend_coins

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.mrostami.geckoincompose.model.TrendCoin
import com.mrostami.geckoincompose.ui.components.StateView
import com.mrostami.geckoincompose.ui.components.TrendCoinItemView
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme

@Composable
fun TrendCoinsWidget(
    modifier: Modifier = Modifier,
    viewModel: TrendCoinsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
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