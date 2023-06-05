package com.mrostami.geckoincompose.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.mrostami.geckoincompose.ui.home.bitcoin_widget.BtcChartWidget
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme

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
        Spacer(modifier = Modifier.size(Dp(16f)))
        MarketDominanceWidget()
        Spacer(modifier = Modifier.size(Dp(8f)))
        BtcChartWidget()
    }
}