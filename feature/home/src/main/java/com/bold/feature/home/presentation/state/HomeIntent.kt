package com.bold.feature.home.presentation.state

sealed class HomeIntent {
    object LoadWeather : HomeIntent()
    object RefreshWeather : HomeIntent()
    data class UpdateSearchQuery(val query: String) : HomeIntent()
    data class SelectLocation(val lat: Double, val lon: Double) : HomeIntent()
    object ClearSearch : HomeIntent()
}
