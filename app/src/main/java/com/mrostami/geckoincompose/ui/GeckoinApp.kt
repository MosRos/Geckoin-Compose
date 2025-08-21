package com.mrostami.geckoincompose.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.mrostami.geckoincompose.model.inDarkMode
import com.mrostami.geckoincompose.ui.navigation.AppNavigation
import com.mrostami.geckoincompose.ui.navigation.MainBottomBar
import com.mrostami.geckoincompose.ui.navigation.MainScreen
import com.mrostami.geckoincompose.ui.navigation.MainTopBar
import com.mrostami.geckoincompose.ui.theme.GeckoinTheme

//@ExperimentalAnimationApi
@Composable
fun GeckoinApp(
    viewModel: MainViewModel
) {
    val themeMode = viewModel.themeMode.collectAsStateWithLifecycle()
    val inDarkMode: Boolean = themeMode.value.inDarkMode(isSystemDark =  isSystemInDarkTheme())
    GeckoinTheme (darkTheme = inDarkMode) {
        val navController = rememberNavController()
        val showBottomNav = rememberSaveable { mutableStateOf(true) }
        Scaffold (
            containerColor = GeckoinTheme.colorScheme.background,
            bottomBar = {
                MainBottomBar(
                    navController = navController
                )
            }
        ) { paddings ->
            AppNavigation(
                navController = navController,
                startDestination = MainScreen.Home.route,
                modifier = Modifier.padding(paddings)
            )
        }
    }
}