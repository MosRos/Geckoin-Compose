package com.mrostami.geckoincompose.ui.market_ranks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mrostami.geckoincompose.model.RankedCoin
import com.mrostami.geckoincompose.ui.base.BaseUiState
import com.mrostami.geckoincompose.ui.components.RankedCoinItemView
import com.mrostami.geckoincompose.ui.navigation.MainTopBar
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun MarketRankScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: CoinRankViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var isLoading by remember { mutableStateOf(false) }

    fun loadMoreItems() {
        viewModel.onNewEvent(event = CoinsRanksEvents.LoadMore)
    }

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
            coinsList = uiState.value.data,
            loadMoreItems = ::loadMoreItems,
            isLoading = uiState.value.state == BaseUiState.State.LOADING
        )
    }


}

@Composable
fun RnaksListView(
    modifier: Modifier,
    listState: LazyListState,
    coinsList: List<RankedCoin>,
    loadMoreItems: () -> Unit,  // Function to load more items
    buffer: Int = 2,  // Buffer to load more items when we get near the end
    isLoading: Boolean,  // Track if items are being loaded
) {

    // Derived state to determine when to load more items
    val shouldLoadMore = remember {
        derivedStateOf {
            // Get the total number of items in the list
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            // Get the index of the last visible item
            val lastVisibleItemIndex =
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            // Check if we have scrolled near the end of the list and more items should be loaded
            lastVisibleItemIndex >= (totalItemsCount - buffer) && !isLoading
        }
    }

    // Launch a coroutine to load more items when shouldLoadMore becomes true
    LaunchedEffect(listState) {
        snapshotFlow { shouldLoadMore.value }
            .distinctUntilChanged()
            .filter { it }  // Ensure that we load more items only when needed
            .collect {
                loadMoreItems()
            }
    }

    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        itemsIndexed(
            items = coinsList,
            key = { index, item -> item.id }
        ) { index, item ->
            RankedCoinItemView(
                coin = item
            )
        }

        // Show a loading indicator at the bottom when items are being loaded
        if (isLoading) {
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
    }
}