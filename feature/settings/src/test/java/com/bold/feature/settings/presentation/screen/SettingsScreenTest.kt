package com.bold.feature.settings.presentation.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bold.core.model.settings.UserSettings
import com.bold.feature.settings.presentation.state.SettingsIntent
import com.bold.feature.settings.presentation.state.SettingsState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysCurrentSettings() {
        val testState = SettingsState(
            userSettings = UserSettings(useCelsius = false, language = "en")
        )

        composeTestRule.setContent {
            SettingsScreenContent(
                state = testState,
                onIntent = {},
                onNavigateBack = {}
            )
        }

        composeTestRule.onNodeWithText("Fahrenheit (°F)").assertIsDisplayed()
        composeTestRule.onNodeWithText("🇺🇸 English", substring = true).assertIsDisplayed()
    }

    @Test
    fun clickingTemperatureDropdownSelectsOption() {
        var capturedIntent: SettingsIntent? = null
        val testState = SettingsState(
            userSettings = UserSettings(useCelsius = true, language = "es")
        )

        composeTestRule.setContent {
            SettingsScreenContent(
                state = testState,
                onIntent = { capturedIntent = it },
                onNavigateBack = {}
            )
        }

        // Click the dropdown to expand
        composeTestRule.onNodeWithText("Celsius (°C)").performClick()
        
        // Click Fahrenheit
        composeTestRule.onNodeWithText("Fahrenheit (°F)").performClick()

        assertEquals(SettingsIntent.UpdateTemperatureUnit(false), capturedIntent)
    }

    @Test
    fun clickingLanguageDropdownSelectsOption() {
        var capturedIntent: SettingsIntent? = null
        val testState = SettingsState(
            userSettings = UserSettings(useCelsius = true, language = "es")
        )

        composeTestRule.setContent {
            SettingsScreenContent(
                state = testState,
                onIntent = { capturedIntent = it },
                onNavigateBack = {}
            )
        }

        // Click the dropdown to expand
        composeTestRule.onNodeWithText("🇪🇸", substring = true).performClick()
        
        // Click English
        composeTestRule.onNodeWithText("🇺🇸 English", substring = true).performClick()

        assertEquals(SettingsIntent.UpdateLanguage("en"), capturedIntent)
    }

    @Test
    fun clickingBackButtonTriggersNavigateBack() {
        var backClicks = 0
        val testState = SettingsState()

        composeTestRule.setContent {
            SettingsScreenContent(
                state = testState,
                onIntent = {},
                onNavigateBack = { backClicks++ }
            )
        }

        composeTestRule.onNodeWithContentDescription("Volver").performClick()
        
        // Debounce prevents second click
        composeTestRule.onNodeWithContentDescription("Volver").performClick()

        assertEquals(1, backClicks)
    }
}
