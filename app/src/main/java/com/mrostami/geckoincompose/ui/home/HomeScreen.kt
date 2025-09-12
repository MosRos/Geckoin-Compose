package com.mrostami.geckoincompose.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mrostami.geckoincompose.ui.home.bitcoin_chart.BtcChartWidget
import com.mrostami.geckoincompose.ui.home.dominance_chart.MarketDominanceWidget
import com.mrostami.geckoincompose.ui.home.trend_coins.TrendCoinsWidget
import com.mrostami.geckoincompose.ui.navigation.MainTopBar
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    modifier: Modifier,
    title: String,
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    var btcWidgetVisibility by remember { mutableStateOf(false) }
    var dominanceWidgetVisibility by remember { mutableStateOf(false) }
    var trendCoinsVisibility by remember { mutableStateOf(false) }

    val animationDelayMillis = 300L // Base delay for staggering
    val animationDurationMillis = 500 // Duration for fade/slide

    LaunchedEffect(Unit) {
        delay(100)
        btcWidgetVisibility = true
        delay(animationDelayMillis)
        dominanceWidgetVisibility = true
        delay(animationDelayMillis)
        trendCoinsVisibility = true
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            MainTopBar(navHostController = navController)
        },
        backgroundColor = GeckoinTheme.colorScheme.background
    ) { paddings ->

        val configuration = LocalConfiguration.current
        val screenWidthDp = configuration.screenWidthDp.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .heightIn(min = screenWidthDp * 4) // set min height to make enter widgets animation smooth
                .padding(paddings)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(Dp(16f)))
            AnimatedVisibility(
                visible = btcWidgetVisibility,
                enter = fadeIn(animationSpec = tween(durationMillis = animationDurationMillis)) +
                        slideInVertically(
                            initialOffsetY = { it / 2 }, // Start from halfway down
                            animationSpec = tween(durationMillis = animationDurationMillis)
                        ),
                exit = fadeOut(animationSpec = tween(durationMillis = animationDurationMillis / 2)) + // Faster exit
                        slideOutVertically(
                            targetOffsetY = { it / 2 },
                            animationSpec = tween(durationMillis = animationDurationMillis / 2)
                        ) // Optional: Define exit if you plan to hide them
            ) {
                BtcChartWidget()
            }

            Spacer(modifier = Modifier.size(Dp(8f)))
            AnimatedVisibility(
                visible = dominanceWidgetVisibility,
                enter = fadeIn(animationSpec = tween(durationMillis = animationDurationMillis)) +
                        slideInVertically(
                            initialOffsetY = { it / 2 },
                            animationSpec = tween(durationMillis = animationDurationMillis)
                        ),
                exit = fadeOut(animationSpec = tween(durationMillis = animationDurationMillis / 2)) +
                        slideOutVertically(
                            targetOffsetY = { it / 2 },
                            animationSpec = tween(durationMillis = animationDurationMillis / 2)
                        )
            ) {
                MarketDominanceWidget()
            }
            Spacer(modifier = Modifier.size(Dp(8f)))
            AnimatedVisibility(
                visible = trendCoinsVisibility,
                enter = fadeIn(animationSpec = tween(durationMillis = animationDurationMillis)) +
                        slideInVertically(
                            initialOffsetY = { it / 2 },
                            animationSpec = tween(durationMillis = animationDurationMillis)
                        ),
                exit = fadeOut(animationSpec = tween(durationMillis = animationDurationMillis / 2)) +
                        slideOutVertically(
                            targetOffsetY = { it / 2 },
                            animationSpec = tween(durationMillis = animationDurationMillis / 2)
                        )
            ) {
                TrendCoinsWidget()
            }
            Spacer(modifier = Modifier.size(Dp(58f)))
        }
    }
}