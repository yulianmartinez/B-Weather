package com.bold.feature.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bold.core.model.settings.SettingsRepository
import com.bold.feature.settings.domain.usecase.UpdateSettingsUseCase
import com.bold.feature.settings.presentation.state.SettingsIntent
import com.bold.feature.settings.presentation.state.SettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val updateSettingsUseCase: UpdateSettingsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.getUserSettings().collect { settings ->
                _state.update { it.copy(isLoading = false, userSettings = settings) }
            }
        }
    }

    fun handleIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.UpdateTemperatureUnit -> {
                viewModelScope.launch {
                    updateSettingsUseCase(intent.useCelsius)
                }
            }
            is SettingsIntent.UpdateLanguage -> {
                viewModelScope.launch {
                    settingsRepository.updateLanguage(intent.language)
                }
            }
        }
    }
}
