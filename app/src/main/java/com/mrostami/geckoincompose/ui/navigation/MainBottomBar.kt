package com.mrostami.geckoincompose.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int
) {
    object Home : MainScreen(
        route = MainDestinations.HOME_ROUTE,
        title = R.string.title_home,
        icon = R.drawable.ic_home,
        selectedIcon = R.drawable.ic_home_selected
    )

    object Market : MainScreen(
        route = MainDestinations.MARKET_ROUTE,
        title = R.string.title_market,
        icon = R.drawable.ic_list,
        selectedIcon = R.drawable.ic_list_selected
    )

    object Search : MainScreen(
        route = MainDestinations.SEARCH_ROUTE,
        title = R.string.title_search,
        icon = R.drawable.ic_search,
        selectedIcon = R.drawable.ic_search_selected
    )

    object Settings : MainScreen(
        route = MainDestinations.SETTINGS,
        title = R.string.title_settings,
        icon = R.drawable.ic_more_horizontal,
        selectedIcon = R.drawable.ic_more_horizontal_selected
    )
}

val mainScreens = listOf(
    MainScreen.Home,
    MainScreen.Market,
    MainScreen.Search,
    MainScreen.Settings
)

@Composable
fun MainTopBar(
    navHostController: NavHostController
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val screenTitle: String = when(currentDestination?.route) {
        MainScreen.Home.route -> stringResource(MainScreen.Home.title)
        MainScreen.Market.route -> stringResource(MainScreen.Market.title)
        MainScreen.Search.route -> stringResource(MainScreen.Search.title)
        MainScreen.Settings.route -> stringResource(MainScreen.Settings.title)
        else -> ""
    }
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        TopAppBar(
            modifier = Modifier.navigationBarsPadding(),
            title = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Text(
                        modifier = Modifier
                            .align(alignment = Alignment.Center)
                            .padding(vertical = 10.dp),
                        text = screenTitle,
                        style = GeckoinTheme.typography.headlineMedium,
                        color = GeckoinTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                    )
                }
            },
            backgroundColor = GeckoinTheme.colorScheme.surface
        )
    }
}

@Composable
fun MainTopBar(
    navHostController: NavController
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val screenTitle: String = when(currentDestination?.route) {
        MainScreen.Home.route -> stringResource(MainScreen.Home.title)
        MainScreen.Market.route -> stringResource(MainScreen.Market.title)
        MainScreen.Search.route -> stringResource(MainScreen.Search.title)
        MainScreen.Settings.route -> stringResource(MainScreen.Settings.title)
        else -> ""
    }
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        TopAppBar(
            modifier = Modifier.navigationBarsPadding(),
            title = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Text(
                        modifier = Modifier
                            .align(alignment = Alignment.Center)
                            .padding(vertical = 10.dp),
                        text = screenTitle,
                        style = GeckoinTheme.typography.headlineMedium,
                        color = GeckoinTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                    )
                }
            },
            backgroundColor = GeckoinTheme.colorScheme.surface
        )
    }
}

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
            modifier = Modifier.navigationBarsPadding(),
            backgroundColor = GeckoinTheme.colorScheme.surface
        ) {
            mainScreens.forEach { screen ->
                addItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navHostController = navController
                )
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
        modifier = Modifier,
        label = {
            BottomNavLabel(
                title = stringResource(id = screen.title),
                isSelected = currentDestination?.route == screen.route
            )
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
            if (currentDestination?.route == screen.route) {
                Icon(painter = painterResource(id = screen.selectedIcon), contentDescription = null)
            } else {
                Icon(painter = painterResource(id = screen.icon), contentDescription = null)
            }
        })
}

@Composable
fun BottomNavLabel(
    title: String,
    isSelected: Boolean
) {
    Text(
        text = title,
        style = if (isSelected) GeckoinTheme.typography.labelMedium else GeckoinTheme.typography.labelSmall,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}