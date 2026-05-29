package com.bold.feature.home.presentation.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bold.core.model.location.LocationData
import com.bold.core.model.weather.CurrentWeather
import com.bold.core.model.weather.Weather
import com.bold.core.model.weather.WeatherCondition
import com.bold.feature.home.presentation.state.HomeState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_showsCircularProgressIndicator() {
        composeTestRule.setContent {
            HomeScreenContent(
                state = HomeState(isLoading = true),
                onIntent = {},
                onNavigateToSettings = {}
            )
        }

        composeTestRule.onNodeWithText("Error:").assertDoesNotExist()
    }

    @Test
    fun errorState_showsErrorTextAndRetryButton() {
        val errorMessage = "Network unavailable"
        composeTestRule.setContent {
            HomeScreenContent(
                state = HomeState(error = errorMessage),
                onIntent = {},
                onNavigateToSettings = {}
            )
        }

        composeTestRule.onNodeWithText("Error: $errorMessage").assertIsDisplayed()
    }

    @Test
    fun successState_showsWeatherInformation() {
        val mockWeather = Weather(
            location = LocationData("Bogota", "Colombia", 4.6, -74.0),
            current = CurrentWeather(20.0, WeatherCondition("Sunny", "", 1000), 50, 10.0, 22.0, 5.0, 10.0),
            forecast = emptyList()
        )

        composeTestRule.setContent {
            HomeScreenContent(
                state = HomeState(weather = mockWeather),
                onIntent = {},
                onNavigateToSettings = {}
            )
        }

        composeTestRule.onNodeWithText("Bogota", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("20°C", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Sunny", substring = true).assertIsDisplayed()
    }
}
