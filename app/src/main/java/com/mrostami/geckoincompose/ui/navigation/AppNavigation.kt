package com.mrostami.geckoincompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mrostami.geckoincompose.ui.home.HomeScreen
import com.mrostami.geckoincompose.ui.market_ranks.MarketRankScreen
import com.mrostami.geckoincompose.ui.search.SearchScreen
import com.mrostami.geckoincompose.ui.settings.SettingsScreen

object MainDestinations {
    const val HOME_ROUTE = "home"
    const val MARKET_ROUTE = "market"
    const val SEARCH_ROUTE = "search"
    const val SETTINGS = "settings"
    const val COIN_DETAIL = "coin/detail"
}

//@ExperimentalAnimationApi
@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        homepage(
            modifier = modifier,
            navController = navController
        )
        market(
            modifier = modifier,
            navController = navController
        )
        search(
            modifier = modifier,
            navController = navController
        )
        settings(
            modifier = modifier,
            navController = navController
        )
    }
}

fun NavGraphBuilder.homepage(
    modifier: Modifier,
    navController: NavController
) {
    composable(
        route = MainScreen.Home.route
    ) {
        HomeScreen(
            modifier = modifier,
            title = stringResource(id = MainScreen.Home.title),
            navController = navController
        )
    }
}

fun NavGraphBuilder.market(
    modifier: Modifier,
    navController: NavController
) {
    composable(
        route = MainScreen.Market.route
    ) {
        MarketRankScreen(
            modifier = modifier,
//            title = stringResource(id = MainScreen.Market.title),
            navController = navController
        )
    }
}

fun NavGraphBuilder.search(
    modifier: Modifier,
    navController: NavController
) {
    composable(route = MainScreen.Search.route) {
        SearchScreen(
            modifier = modifier,
            title = stringResource(id = MainScreen.Search.title),
            navController = navController
        )
    }
}

fun NavGraphBuilder.settings(
    modifier: Modifier,
    navController: NavController
) {
    composable(route = MainScreen.Settings.route) {
        SettingsScreen(
            modifier = modifier,
            navController = navController
        )
    }
}