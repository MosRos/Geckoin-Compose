package com.mrostami.geckoincompose.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import com.mrostami.geckoincompose.ui.home.bitcoin_chart.BtcChartWidget
import com.mrostami.geckoincompose.ui.home.trend_coins.TrendCoinsWidget
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme

@Composable
fun HomeScreen(
    title: String,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = GeckoinTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.size(Dp(16f)))
            BtcChartWidget()
            Spacer(modifier = Modifier.size(Dp(8f)))
            MarketDominanceWidget()
            Spacer(modifier = Modifier.size(Dp(8f)))
            TrendCoinsWidget()
            Spacer(modifier = Modifier.size(Dp(58f)))
        }
    }
}