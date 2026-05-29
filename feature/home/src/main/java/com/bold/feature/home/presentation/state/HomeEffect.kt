package com.bold.feature.home.presentation.state

sealed class HomeEffect {
    data class ShowToast(val message: String) : HomeEffect()
}
