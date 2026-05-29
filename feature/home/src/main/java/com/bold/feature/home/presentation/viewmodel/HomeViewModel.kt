package com.bold.feature.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bold.core.common.result.Resource
import com.bold.feature.home.domain.usecase.GetCurrentWeatherUseCase
import com.bold.feature.home.presentation.state.HomeIntent
import com.bold.feature.home.presentation.state.HomeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import com.bold.core.model.settings.SettingsRepository

import com.bold.feature.home.domain.usecase.SearchLocationsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class HomeViewModel(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val searchLocationsUseCase: SearchLocationsUseCase,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            var isFirstEmission = true
            settingsRepository.getUserSettings().collect { settings ->
                _state.update { it.copy(useCelsius = settings.useCelsius) }
                
                if (isFirstEmission) {
                    isFirstEmission = false
                    val lat = settings.lastLat
                    val lon = settings.lastLon
                    if (lat != null && lon != null && _state.value.weather == null) {
                        fetchWeather(lat, lon)
                    }
                }
            }
        }
    }

    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadWeather -> fetchWeather(null, null)
            is HomeIntent.RefreshWeather -> {
                val current = _state.value.weather?.location
                if (current != null) {
                    fetchWeather(current.lat, current.lon)
                }
            }
            is HomeIntent.UpdateSearchQuery -> handleSearchQuery(intent.query)
            is HomeIntent.SelectLocation -> {
                _state.update { it.copy(searchQuery = "", searchResults = emptyList(), isSearching = false) }
                fetchWeather(intent.lat, intent.lon)
            }
            is HomeIntent.ClearSearch -> {
                searchJob?.cancel()
                _state.update { it.copy(searchQuery = "", searchResults = emptyList(), isSearching = false) }
            }
        }
    }

    private fun handleSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        
        if (query.length < 3) {
            _state.update { it.copy(searchResults = emptyList(), isSearching = false) }
            return
        }

        searchJob = viewModelScope.launch {
            delay(400) // debounce
            searchLocationsUseCase(query).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _state.update { it.copy(isSearching = true) }
                    is Resource.Success -> _state.update { 
                        it.copy(isSearching = false, searchResults = resource.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(isSearching = false, searchResults = emptyList()) }
                    }
                }
            }
        }
    }

    private fun fetchWeather(targetLat: Double?, targetLon: Double?) {
        if (targetLat == null || targetLon == null) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            val lat: Double = targetLat
            val lon: Double = targetLon

            getCurrentWeatherUseCase(lat, lon).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _state.update { it.copy(isLoading = false, weather = resource.data) }
                        settingsRepository.saveLastLocation(lat, lon)
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(isLoading = false, error = resource.message) }
                    }
                }
            }
        }
    }
}
