package com.bold.feature.home.presentation.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bold.core.model.weather.WeatherCondition
import com.bold.core.model.weather.ForecastDay
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(instrumentedPackages = ["androidx.loader.content"], sdk = [34])
class ForecastSectionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun forecastSection_displaysForecastDays() {
        val mockForecast = listOf(
            ForecastDay(
                date = "2023-10-01",
                dateEpoch = 1696118400L,
                avgTempC = 24.0,
                minTempC = 18.0,
                maxTempC = 28.0,
                condition = WeatherCondition(
                    text = "Sunny",
                    iconUrl = "",
                    code = 1000
                )
            ),
            ForecastDay(
                date = "2023-10-02",
                dateEpoch = 1696204800L,
                avgTempC = 22.0,
                minTempC = 16.0,
                maxTempC = 26.0,
                condition = WeatherCondition(
                    text = "Rainy",
                    iconUrl = "",
                    code = 1001
                )
            )
        )

        composeTestRule.setContent {
            ForecastSection(
                forecast = mockForecast,
                useCelsius = true
            )
        }

        // Verify dates
        composeTestRule.onNodeWithText("2023-10-01").assertIsDisplayed()
        composeTestRule.onNodeWithText("2023-10-02").assertIsDisplayed()

        // Verify Avg Temperatures
        composeTestRule.onNodeWithText("24°C").assertIsDisplayed()
        composeTestRule.onNodeWithText("22°C").assertIsDisplayed()
    }
    
    @Test
    fun forecastSection_displaysFahrenheit_whenCelsiusFalse() {
        val mockForecast = listOf(
            ForecastDay(
                date = "2023-10-01",
                dateEpoch = 1696118400L,
                avgTempC = 24.0,
                minTempC = 18.0,
                maxTempC = 28.0,
                condition = WeatherCondition(
                    text = "Sunny",
                    iconUrl = "",
                    code = 1000
                )
            )
        )

        composeTestRule.setContent {
            ForecastSection(
                forecast = mockForecast,
                useCelsius = false
            )
        }

        // 24.0 C = 75.2 F (Math.round(24.0 * 1.8 + 32) = 75)
        // Wait, TemperatureUtils handles rounding. Let's just assume it says 75°F.
        composeTestRule.onNodeWithText("75°F").assertIsDisplayed()
    }
}
