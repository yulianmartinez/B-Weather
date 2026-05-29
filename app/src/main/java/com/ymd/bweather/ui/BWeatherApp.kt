package com.ymd.bweather.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ymd.bweather.navigation.AppNavHost

@Composable
fun BWeatherApp(
    appState: AppState = rememberAppState(),
    modifier: Modifier = Modifier
) {
    AppNavHost(
        navController = appState.navController,
        modifier = modifier
    )
}
