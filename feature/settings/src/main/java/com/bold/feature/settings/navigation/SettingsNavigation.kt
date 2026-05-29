package com.bold.feature.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bold.core.common.navigation.AppDestinations
import com.bold.feature.settings.presentation.screen.SettingsScreen

fun NavGraphBuilder.settingsScreen(onNavigateBack: () -> Unit) {
    composable(route = AppDestinations.SETTINGS) {
        SettingsScreen(
            onNavigateBack = onNavigateBack
        )
    }
}
