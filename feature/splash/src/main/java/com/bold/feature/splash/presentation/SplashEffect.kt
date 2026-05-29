package com.bold.feature.splash.presentation

sealed interface SplashEffect {
    data object NavigateToHome : SplashEffect
}
