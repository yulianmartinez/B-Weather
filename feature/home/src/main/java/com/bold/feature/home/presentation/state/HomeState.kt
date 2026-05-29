package com.bold.feature.home.presentation.state

import com.bold.core.model.weather.Weather

import com.bold.core.model.location.LocationData

data class HomeState(
    val isLoading: Boolean = false,
    val weather: Weather? = null,
    val error: String? = null,
    val useCelsius: Boolean = true,
    val searchQuery: String = "",
    val searchResults: List<LocationData> = emptyList(),
    val isSearching: Boolean = false
)
