package com.bold.feature.home.data.repository

import app.cash.turbine.test
import com.bold.core.common.result.Resource
import com.bold.core.model.settings.SettingsRepository
import com.bold.core.model.settings.UserSettings
import com.bold.core.network.api.SearchApi
import com.bold.core.network.api.WeatherApi
import com.bold.core.network.dto.WeatherApiConditionDto
import com.bold.core.network.dto.WeatherApiCurrentDto
import com.bold.core.network.dto.WeatherApiDayDto
import com.bold.core.network.dto.WeatherApiForecastDayDto
import com.bold.core.network.dto.WeatherApiForecastDto
import com.bold.core.network.dto.WeatherApiLocationDto
import com.bold.core.network.dto.WeatherApiResponseDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class WeatherRepositoryImplTest {

    private lateinit var weatherApi: WeatherApi
    private lateinit var searchApi: SearchApi
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var repository: WeatherRepositoryImpl

    @Before
    fun setUp() {
        weatherApi = mockk()
        searchApi = mockk()
        settingsRepository = mockk()
        repository = WeatherRepositoryImpl(weatherApi, searchApi, settingsRepository)
    }

    @Test
    fun `getWeather should emit Loading then Success when API call succeeds`() = runTest {
        // Given
        val lat = 4.6097
        val lon = -74.0817
        
        coEvery { settingsRepository.getUserSettings() } returns flowOf(UserSettings(language = "es"))
        
        val mockResponseDto = WeatherApiResponseDto(
            location = WeatherApiLocationDto("Bogota", "Colombia", lat, lon),
            current = WeatherApiCurrentDto(
                tempC = 20.0,
                condition = WeatherApiConditionDto("Sunny", "icon.png", 1000),
                humidity = 50,
                windKph = 10.0,
                feelsLikeC = 22.0,
                uv = 5.0,
                visKm = 10.0
            ),
            forecast = WeatherApiForecastDto(
                forecastday = listOf(
                    WeatherApiForecastDayDto(
                        date = "2026-05-28",
                        dateEpoch = 123456789L,
                        day = WeatherApiDayDto(
                            avgTempC = 21.0,
                            maxTempC = 25.0,
                            minTempC = 15.0,
                            condition = WeatherApiConditionDto("Sunny", "icon.png", 1000)
                        )
                    )
                )
            )
        )
        
        coEvery { weatherApi.getForecast(query = "$lat,$lon", lang = "es") } returns mockResponseDto

        // When & Then
        repository.getWeather(lat, lon).test {
            assertEquals(Resource.Loading, awaitItem())
            val successItem = awaitItem()
            assertTrue(successItem is Resource.Success)
            assertEquals("Bogota", (successItem as Resource.Success).data.location.name)
            awaitComplete()
        }
    }

    @Test
    fun `getWeather should emit Loading then Error when API call fails`() = runTest {
        // Given
        val lat = 4.6097
        val lon = -74.0817
        
        coEvery { settingsRepository.getUserSettings() } returns flowOf(UserSettings(language = "es"))
        coEvery { weatherApi.getForecast(any(), any()) } throws java.io.IOException("Network Error")

        // When & Then
        repository.getWeather(lat, lon).test {
            assertEquals(Resource.Loading, awaitItem())
            val errorItem = awaitItem()
            assertTrue(errorItem is Resource.Error)
            assertEquals("Network Error", (errorItem as Resource.Error).message)
            awaitComplete()
        }
    }

    @Test
    fun `getWeather should emit Error when API returns HttpException`() = runTest {
        // Given
        val lat = 4.6097
        val lon = -74.0817
        
        coEvery { settingsRepository.getUserSettings() } returns flowOf(UserSettings(language = "es"))
        
        val responseBody = "Not Found".toResponseBody(null)
        val retrofitResponse = retrofit2.Response.error<WeatherApiResponseDto>(404, responseBody)
        coEvery { weatherApi.getForecast(any(), any()) } throws retrofit2.HttpException(retrofitResponse)

        // When & Then
        repository.getWeather(lat, lon).test {
            assertEquals(Resource.Loading, awaitItem())
            val errorItem = awaitItem()
            assertTrue(errorItem is Resource.Error)
            assertTrue((errorItem as Resource.Error).message.contains("HTTP 404"))
            awaitComplete()
        }
    }

    @Test
    fun `searchLocations should emit Error when API throws IOException`() = runTest {
        // Given
        coEvery { searchApi.searchLocation(any()) } throws java.io.IOException("No internet connection")

        // When & Then
        repository.searchLocations("Bogota").test {
            assertEquals(Resource.Loading, awaitItem())
            val errorItem = awaitItem()
            assertTrue(errorItem is Resource.Error)
            assertEquals("No internet connection", (errorItem as Resource.Error).message)
            awaitComplete()
        }
    }

    @Test
    fun `searchLocations should emit Error when API throws HttpException`() = runTest {
        // Given
        val responseBody = "Server Error".toResponseBody(null)
        val retrofitResponse = retrofit2.Response.error<List<com.bold.core.network.dto.SearchLocationDto>>(500, responseBody)
        coEvery { searchApi.searchLocation(any()) } throws retrofit2.HttpException(retrofitResponse)

        // When & Then
        repository.searchLocations("Invalid City").test {
            assertEquals(Resource.Loading, awaitItem())
            val errorItem = awaitItem()
            assertTrue(errorItem is Resource.Error)
            assertTrue((errorItem as Resource.Error).message.contains("HTTP 500"))
            awaitComplete()
        }
    }
}
