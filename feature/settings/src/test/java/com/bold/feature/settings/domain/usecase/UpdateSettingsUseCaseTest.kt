package com.bold.feature.settings.domain.usecase

import com.bold.core.model.settings.SettingsRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateSettingsUseCaseTest {

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var updateSettingsUseCase: UpdateSettingsUseCase

    @Before
    fun setUp() {
        settingsRepository = mockk(relaxed = true)
        updateSettingsUseCase = UpdateSettingsUseCase(settingsRepository)
    }

    @Test
    fun `invoke should call updateTemperatureUnit on repository`() = runTest {
        // Given
        val useCelsius = false

        // When
        updateSettingsUseCase(useCelsius)

        // Then
        coVerify(exactly = 1) { settingsRepository.updateTemperatureUnit(useCelsius) }
    }
}
