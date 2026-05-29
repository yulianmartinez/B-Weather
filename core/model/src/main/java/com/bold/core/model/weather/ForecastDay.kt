package com.bold.core.model.weather

data class ForecastDay(
    val date: String,
    val dateEpoch: Long,
    val avgTempC: Double,
    val minTempC: Double,
    val maxTempC: Double,
    val condition: WeatherCondition
)
