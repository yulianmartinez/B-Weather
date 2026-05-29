package com.bold.feature.splash.presentation

sealed interface SplashIntent {
    data object LoadApp : SplashIntent
}
