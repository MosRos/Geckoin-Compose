package com.mrostami.geckoincompose.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mrostami.geckoincompose.R
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme


sealed class MainScreen(
    val route: String,
    @StringRes val title: Int,
    @DrawableRes val icon: Int
) {
    object Home : MainScreen(
        route = MainDestinations.HOME_ROUTE,
        title = R.string.title_home,
        icon = R.drawable.ic_home
    )

    object Market : MainScreen(
        route = MainDestinations.MARKET_ROUTE,
        title = R.string.title_market,
        icon = R.drawable.ic_list
    )

    object Search : MainScreen(
        route = MainDestinations.SEARCH_ROUTE,
        title = R.string.title_search,
        icon = R.drawable.ic_search
    )

    object Settings : MainScreen(
        route = MainDestinations.SETTINGS,
        title = R.string.title_settings,
        icon = R.drawable.ic_more_horizontal
    )
}

val mainScreens = listOf(
    MainScreen.Home,
    MainScreen.Market,
    MainScreen.Search,
    MainScreen.Settings
)

@Composable
fun MainBottomBar(
    navController: NavHostController
) {
    AnimatedVisibility(
        visible = true
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        BottomNavigation(
            modifier = Modifier.navigationBarsPadding()
        ) {
            mainScreens.forEach { screen ->
                addItem(screen = screen, currentDestination = currentDestination, navHostController = navController)
            }
        }
    }
}

@Composable
fun RowScope.addItem(
    screen: MainScreen,
    currentDestination: NavDestination?,
    navHostController: NavHostController
) {
    BottomNavigationItem(
        modifier = Modifier.background(color = MaterialTheme.colors.surface),
        label = {
            BottomNavLabel(title = stringResource(id = screen.title))
        },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        selectedContentColor = GeckoinTheme.colorScheme.primary,
        unselectedContentColor = GeckoinTheme.customColors.textSecondary,
        onClick = {
            if (currentDestination?.route != screen.route) {
                navHostController.navigate(screen.route) {
                    popUpTo(navHostController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        },
        icon = {
            Icon(painter = painterResource(id = screen.icon), contentDescription = null)
        })
}

@Composable
fun BottomNavLabel(
    title: String
) {
    Text(
        text = title,
        style = MaterialTheme.typography.caption,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}