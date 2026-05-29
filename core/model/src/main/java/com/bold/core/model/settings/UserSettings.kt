package com.bold.core.model.settings

data class UserSettings(
    val useCelsius: Boolean = true,
    val language: String = "es",
    val lastLat: Double? = null,
    val lastLon: Double? = null
)
