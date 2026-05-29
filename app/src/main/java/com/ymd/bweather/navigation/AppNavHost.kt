package com.ymd.bweather.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.bold.core.common.navigation.AppDestinations
import com.bold.feature.splash.navigation.splashScreen
import com.bold.feature.home.navigation.homeScreen
import com.bold.feature.settings.navigation.settingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = AppDestinations.SPLASH
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        splashScreen(
            onNavigateToHome = { 
                navController.navigate(AppDestinations.HOME) {
                    popUpTo(AppDestinations.SPLASH) { inclusive = true }
                }
            }
        )
        homeScreen(
            onNavigateToSettings = { navController.navigate("settings") }
        )
        settingsScreen(
            onNavigateBack = { navController.popBackStack() }
        )
    }
}
