package com.bold.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bold.core.common.navigation.AppDestinations
import com.bold.feature.home.presentation.screen.HomeScreen
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.homeScreen(onNavigateToSettings: () -> Unit) {
    composable(route = AppDestinations.HOME) {
        HomeScreen(
            viewModel = koinViewModel(),
            onNavigateToSettings = onNavigateToSettings
        )
    }
}
