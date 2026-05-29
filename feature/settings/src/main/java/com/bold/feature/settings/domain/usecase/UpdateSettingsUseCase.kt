package com.bold.feature.settings.domain.usecase

import com.bold.core.model.settings.SettingsRepository

class UpdateSettingsUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(useCelsius: Boolean) {
        repository.updateTemperatureUnit(useCelsius)
    }
}
