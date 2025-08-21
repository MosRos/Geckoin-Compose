package com.mrostami.geckoincompose.ui.search

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Divider
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.mrostami.geckoincompose.model.Coin
import com.mrostami.geckoincompose.ui.components.SearchWidget
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme
import kotlinx.coroutines.delay

@Composable
fun SearchScreen(
    navController: NavController,
    modifier: Modifier = Modifier.background(color = GeckoinTheme.colorScheme.background),
    title: String = "Search",
    viewModel: SearchViewModel = hiltViewModel()
) {
    val showProgressBar = remember { mutableStateOf(true) }

    Scaffold(
        modifier = modifier
            .background(color = GeckoinTheme.colorScheme.background),
        topBar = {
            SearchAppBar(
                viewModel = viewModel,
                showProgress = showProgressBar
            )
        },
        backgroundColor = GeckoinTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = GeckoinTheme.colorScheme.background)
                .padding(it)
        ) {
            Divider(
                modifier = Modifier
                    .align(alignment = Alignment.Start),
                thickness = 1.dp,
                color = GeckoinTheme.colorScheme.outline
            )
            Crossfade(
                targetState = showProgressBar,
                animationSpec = tween(durationMillis = 300), label = ""
            ) { loading ->
                if (loading.value) {
                    LinearProgressIndicator(
                        backgroundColor = GeckoinTheme.colorScheme.background,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            CoinList(
                viewModel = viewModel,
                onPagingLoading = showProgressBar
            )
        }
    }
}


@Composable
fun SearchAppBar(
    viewModel: SearchViewModel,
    modifier: Modifier = Modifier,
    showProgress: MutableState<Boolean> = mutableStateOf(false)
) {
    val searchQuery = remember { mutableStateOf("") }
    val searchAction: (String) -> Unit = { query ->
        searchQuery.value = query
        showProgress.value = true
        viewModel.searchCoins(query)
    }
    Box(
        modifier = modifier
            .background(color = GeckoinTheme.colorScheme.surface)
            .padding(vertical = 10.dp)
    ) {
        SearchWidget(
            value = searchQuery.value,
            onQueryChanged = searchAction,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .align(alignment = Alignment.Center)
        )
    }
}

@Composable
fun CoinList(
    viewModel: SearchViewModel,
    modifier: Modifier = Modifier,
    onPagingLoading: MutableState<Boolean> = mutableStateOf(true),
    onCoinClicked: (Coin) -> Unit = {}
) {
    val coinsPaging: LazyPagingItems<Coin> = viewModel.uiState.collectAsLazyPagingItems().apply {
        onPagingLoading.value = this.loadState.append is LoadState.Loading
    }


    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2)
    ) {
        items(
            count = coinsPaging.itemCount
        ) { index ->
            coinsPaging.itemSnapshotList.items.getOrNull(index)?.let { coin ->
                CoinsGridItem(
                    coin = coin
                )
            }
        }
    }
}

@Composable
fun CoinsGridItem(
    coin: Coin,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(5.dp)
            .background(
                color = GeckoinTheme.colorScheme.surface,
                shape = GeckoinTheme.shapes.medium
            )
            .border(
                width = 1.dp,
                color = GeckoinTheme.customColors.dividerColor,
                shape = GeckoinTheme.shapes.medium
            )
            .clickable(enabled = true) {

            }
    ) {
        Text(
            text = coin.name.toString(),
            style = GeckoinTheme.typography.titleSmall,
            color = GeckoinTheme.customColors.textPrimary,
            maxLines = 1,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                .align(alignment = Alignment.Start)
        )
        Text(
            text = "$ " + coin.symbol.toString(),
            style = GeckoinTheme.typography.bodyMedium,
            color = GeckoinTheme.customColors.textSecondary,
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, top = 5.dp, bottom = 8.dp)
                .align(alignment = Alignment.Start)
        )
    }
}