package com.bold.feature.settings.presentation.state

sealed class SettingsIntent {
    data class UpdateTemperatureUnit(val useCelsius: Boolean) : SettingsIntent()
    data class UpdateLanguage(val language: String) : SettingsIntent()
}
