package com.bold.feature.home.domain.repository

import com.bold.core.common.result.Resource
import com.bold.core.model.weather.Weather
import kotlinx.coroutines.flow.Flow

import com.bold.core.model.location.LocationData

interface WeatherRepository {
    fun getWeather(lat: Double, lon: Double): Flow<Resource<Weather>>
    fun searchLocations(query: String): Flow<Resource<List<LocationData>>>
}
