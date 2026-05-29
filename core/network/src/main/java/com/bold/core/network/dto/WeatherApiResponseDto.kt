package com.bold.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherApiResponseDto(
    val location: WeatherApiLocationDto,
    val current: WeatherApiCurrentDto,
    val forecast: WeatherApiForecastDto
)

@Serializable
data class WeatherApiLocationDto(
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double
)

@Serializable
data class WeatherApiCurrentDto(
    @SerialName("temp_c") val tempC: Double,
    val condition: WeatherApiConditionDto,
    val humidity: Int = 0,
    @SerialName("wind_kph") val windKph: Double = 0.0,
    @SerialName("feelslike_c") val feelsLikeC: Double = 0.0,
    val uv: Double = 0.0,
    @SerialName("vis_km") val visKm: Double = 0.0
)

@Serializable
data class WeatherApiConditionDto(
    val text: String,
    val icon: String,
    val code: Int
)

@Serializable
data class WeatherApiForecastDto(
    val forecastday: List<WeatherApiForecastDayDto>
)

@Serializable
data class WeatherApiForecastDayDto(
    val date: String,
    @SerialName("date_epoch") val dateEpoch: Long,
    val day: WeatherApiDayDto
)

@Serializable
data class WeatherApiDayDto(
    @SerialName("avgtemp_c") val avgTempC: Double,
    @SerialName("maxtemp_c") val maxTempC: Double = 0.0,
    @SerialName("mintemp_c") val minTempC: Double = 0.0,
    val condition: WeatherApiConditionDto
)
