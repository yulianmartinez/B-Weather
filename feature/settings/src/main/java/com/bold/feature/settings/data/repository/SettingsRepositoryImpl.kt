package com.bold.feature.settings.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bold.core.model.settings.UserSettings
import com.bold.core.model.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private val useCelsiusKey = booleanPreferencesKey("use_celsius")
    private val languageKey = stringPreferencesKey("language")
    private val lastLatKey = doublePreferencesKey("last_lat")
    private val lastLonKey = doublePreferencesKey("last_lon")

    override fun getUserSettings(): Flow<UserSettings> {
        return dataStore.data.map { preferences ->
            val useCelsius = preferences[useCelsiusKey] ?: true
            val language = preferences[languageKey] ?: "es"
            val lastLat = preferences[lastLatKey]
            val lastLon = preferences[lastLonKey]
            UserSettings(
                useCelsius = useCelsius, 
                language = language,
                lastLat = lastLat,
                lastLon = lastLon
            )
        }
    }

    override suspend fun updateTemperatureUnit(useCelsius: Boolean) {
        dataStore.edit { preferences ->
            preferences[useCelsiusKey] = useCelsius
        }
    }

    override suspend fun updateLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[languageKey] = language
        }
    }

    override suspend fun saveLastLocation(lat: Double, lon: Double) {
        dataStore.edit { preferences ->
            preferences[lastLatKey] = lat
            preferences[lastLonKey] = lon
        }
    }
}
