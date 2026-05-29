package com.bold.feature.settings.presentation.state

import com.bold.core.model.settings.UserSettings

data class SettingsState(
    val isLoading: Boolean = true,
    val userSettings: UserSettings = UserSettings(),
    val error: String? = null
)
