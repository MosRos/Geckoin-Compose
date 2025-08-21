package com.mrostami.geckoincompose.ui.market_ranks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.mrostami.geckoincompose.model.RankedCoin
import com.mrostami.geckoincompose.ui.components.RankedCoinItemView
import com.mrostami.geckoincompose.ui.navigation.MainTopBar
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme

@Composable
fun MarketRankScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: CoinRankViewModel = hiltViewModel()
) {
    val pagedCoins: LazyPagingItems<RankedCoin> = viewModel.rankedCoinsStateFlow().collectAsLazyPagingItems()
    val listState = rememberLazyListState()

    Scaffold(
        modifier = modifier,
        backgroundColor = GeckoinTheme.colorScheme.background,
        topBar = {
            MainTopBar(navHostController = navController)
        }
    ) { paddings ->
        RnaksListView(
            modifier = Modifier.padding(paddings),
            listState = listState,
            pagedCoins = pagedCoins
        )
    }


}

@Composable
fun RnaksListView(
    modifier: Modifier,
    listState: LazyListState,
    pagedCoins: LazyPagingItems<RankedCoin>
) {
    LazyColumn(
        modifier = modifier.background(color = GeckoinTheme.colorScheme.background),
        state = listState
    ) {
        items(
            items = pagedCoins,
            key = {
                it.id
            }
        ) {coin ->
            coin?.let { rankedCoin ->
                RankedCoinItemView(
                    coin = rankedCoin
                )
            }
        }

        when(val state = pagedCoins.loadState.refresh) { // First Load
            is LoadState.Error -> {

            }
            is LoadState.Loading -> {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Paging Loading",
                            style = GeckoinTheme.typography.bodyLarge,
                            color = GeckoinTheme.customColors.textSecondary,
                            modifier = Modifier.padding(8.dp)
                        )
                        CircularProgressIndicator(color = GeckoinTheme.customColors.textSecondary)
                    }
                }
            }
            else -> {

            }
        }

        when(val state = pagedCoins.loadState.append) { // Pagination
            is LoadState.Error -> {

            }
            is LoadState.Loading -> {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Paging Loading",
                            style = GeckoinTheme.typography.bodyLarge,
                            color = GeckoinTheme.customColors.textSecondary,
                            modifier = Modifier.padding(8.dp)
                        )
                        CircularProgressIndicator(color = GeckoinTheme.customColors.textSecondary)
                    }
                }
            }
            else -> {}
        }

    }
}