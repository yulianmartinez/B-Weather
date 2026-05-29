package com.bold.core.model.weather

import com.bold.core.model.location.LocationData

data class Weather(
    val location: LocationData,
    val current: CurrentWeather,
    val forecast: List<ForecastDay>
)

data class CurrentWeather(
    val tempC: Double,
    val condition: WeatherCondition,
    val humidity: Int,
    val windKph: Double,
    val feelsLikeC: Double,
    val uv: Double,
    val visibilityKm: Double
)
