package com.bold.feature.home.presentation.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bold.core.model.location.LocationData
import com.bold.core.model.weather.WeatherCondition
import com.bold.core.model.weather.CurrentWeather
import com.bold.core.model.weather.ForecastDay
import com.bold.core.model.weather.Weather
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(instrumentedPackages = ["androidx.loader.content"], sdk = [34])
class CurrentWeatherSectionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun currentWeather_displaysCorrectData() {
        val mockWeather = Weather(
            location = LocationData("Medellin", "Colombia", 6.2, -75.5),
            current = CurrentWeather(
                tempC = 25.0,
                condition = WeatherCondition("Sunny", "//icon.png", 1000),
                windKph = 10.5,
                humidity = 60,
                feelsLikeC = 26.0,
                visibilityKm = 10.0,
                uv = 5.0
            ),
            forecast = listOf(
                ForecastDay("2023-10-01", 1696118400L, 24.0, 18.0, 28.0, WeatherCondition("Sunny", "", 1000))
            )
        )

        composeTestRule.setContent {
            CurrentWeatherSection(
                weather = mockWeather,
                useCelsius = true
            )
        }

        // Verify Location Name
        composeTestRule.onNodeWithText("Medellin").assertIsDisplayed()
        
        // Verify Condition text
        composeTestRule.onNodeWithText("Sunny").assertIsDisplayed()
        
        // Verify Temperatures
        composeTestRule.onNodeWithText("25°C").assertIsDisplayed() // Current Temp
        composeTestRule.onNodeWithText("28°C").assertIsDisplayed() // Max Temp
        composeTestRule.onNodeWithText("18°C").assertIsDisplayed() // Min Temp
        
        // Verify Details
        composeTestRule.onNodeWithText("60%").assertIsDisplayed() // Humidity
        composeTestRule.onNodeWithText("10.5 km/h").assertIsDisplayed() // Wind
        composeTestRule.onNodeWithText("26°C").assertIsDisplayed() // Feels Like
        composeTestRule.onNodeWithText("10.0 km").assertIsDisplayed() // Visibility
    }
}
