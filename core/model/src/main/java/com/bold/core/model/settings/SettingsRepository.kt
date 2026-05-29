package com.bold.core.model.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getUserSettings(): Flow<UserSettings>
    suspend fun updateTemperatureUnit(useCelsius: Boolean)
    suspend fun updateLanguage(language: String)
    suspend fun saveLastLocation(lat: Double, lon: Double)
}
