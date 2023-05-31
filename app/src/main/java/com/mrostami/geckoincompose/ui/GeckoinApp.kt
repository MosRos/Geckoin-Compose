package com.mrostami.geckoincompose.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.mrostami.geckoincompose.ui.navigation.AppNavigation
import com.mrostami.geckoincompose.ui.navigation.MainBottomBar
import com.mrostami.geckoincompose.ui.navigation.MainScreen
import com.mrostami.geckoincompose.ui.theme.GeckoinComposeTheme

//@ExperimentalAnimationApi
@Composable
fun GeckoinApp(

) {
    GeckoinComposeTheme {
        val navController = rememberNavController()
        val showBottomNav = rememberSaveable { mutableStateOf(true) }
        Scaffold (
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                MainBottomBar(navController = navController)
            }
        ) {
            AppNavigation(
                navController = navController,
                startDestination = MainScreen.Home.route,
                modifier = Modifier.padding(it)
            )
        }
    }
}