package com.bold.feature.settings.presentation.viewmodel

import app.cash.turbine.test
import com.bold.core.model.settings.SettingsRepository
import com.bold.core.model.settings.UserSettings
import com.bold.feature.settings.domain.usecase.UpdateSettingsUseCase
import com.bold.feature.settings.presentation.state.SettingsIntent
import com.bold.feature.settings.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var updateSettingsUseCase: UpdateSettingsUseCase
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        settingsRepository = mockk(relaxed = true)
        updateSettingsUseCase = mockk(relaxed = true)

        coEvery { settingsRepository.getUserSettings() } returns flowOf(
            UserSettings(useCelsius = true, language = "es")
        )
    }

    @Test
    fun `init should load user settings from repository`() = runTest {
        // When
        viewModel = SettingsViewModel(settingsRepository, updateSettingsUseCase)

        // Then
        viewModel.state.test {
            val state = expectMostRecentItem()
            assertFalse(state.isLoading)
            assertEquals(true, state.userSettings.useCelsius)
            assertEquals("es", state.userSettings.language)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `UpdateTemperatureUnit intent should call usecase`() = runTest {
        // Given
        viewModel = SettingsViewModel(settingsRepository, updateSettingsUseCase)

        // When
        viewModel.handleIntent(SettingsIntent.UpdateTemperatureUnit(false))

        // Then
        coVerify(exactly = 1) { updateSettingsUseCase.invoke(false) }
    }

    @Test
    fun `UpdateLanguage intent should call repository`() = runTest {
        // Given
        viewModel = SettingsViewModel(settingsRepository, updateSettingsUseCase)

        // When
        viewModel.handleIntent(SettingsIntent.UpdateLanguage("en"))

        // Then
        coVerify(exactly = 1) { settingsRepository.updateLanguage("en") }
    }
}
