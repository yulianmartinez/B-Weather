package com.bold.feature.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bold.core.common.navigation.AppDestinations
import com.bold.feature.splash.presentation.SplashScreen

fun NavGraphBuilder.splashScreen(onNavigateToHome: () -> Unit) {
    composable(AppDestinations.SPLASH) {
        SplashScreen(onNavigateToHome = onNavigateToHome)
    }
}
