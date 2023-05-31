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
        homepage(navController = navController)
        market(navController = navController)
        search(navController = navController)
        settings(navController = navController)
    }
}

fun NavGraphBuilder.homepage(
    navController: NavController
) {
    composable(
        route = MainScreen.Home.route
    ) {
        HomeScreen(title = stringResource(id = MainScreen.Home.title))
    }
}

fun NavGraphBuilder.market(
    navController: NavController
) {
    composable(
        route = MainScreen.Market.route
    ) {
        HomeScreen(title = stringResource(id = MainScreen.Market.title))
    }
}

fun NavGraphBuilder.search(
    navController: NavController
) {
    composable(route = MainScreen.Search.route) {
        HomeScreen(title = stringResource(id = MainScreen.Search.title))
    }
}

fun NavGraphBuilder.settings(
    navController: NavController
) {
    composable(route = MainScreen.Settings.route) {
        HomeScreen(title = stringResource(id = MainScreen.Settings.title))
    }
}