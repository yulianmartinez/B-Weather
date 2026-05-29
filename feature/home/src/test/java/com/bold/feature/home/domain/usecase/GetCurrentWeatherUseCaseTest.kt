package com.bold.feature.home.domain.usecase

import app.cash.turbine.test
import com.bold.core.common.result.Resource
import com.bold.core.model.location.LocationData
import com.bold.core.model.weather.CurrentWeather
import com.bold.core.model.weather.Weather
import com.bold.core.model.weather.WeatherCondition
import com.bold.feature.home.domain.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetCurrentWeatherUseCaseTest {

    private lateinit var repository: WeatherRepository
    private lateinit var useCase: GetCurrentWeatherUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetCurrentWeatherUseCase(repository)
    }

    @Test
    fun `invoke should return repository weather flow`() = runTest {
        // Given
        val lat = 4.6097
        val lon = -74.0817
        val mockWeather = Weather(
            location = LocationData("Bogota", "Colombia", lat, lon),
            current = CurrentWeather(
                tempC = 20.0,
                condition = WeatherCondition("Sunny", "", 1000),
                humidity = 50,
                windKph = 10.0,
                feelsLikeC = 22.0,
                uv = 5.0,
                visibilityKm = 10.0
            ),
            forecast = emptyList()
        )
        
        coEvery { repository.getWeather(lat, lon) } returns flowOf(
            Resource.Loading,
            Resource.Success(mockWeather)
        )

        // When & Then
        useCase(lat, lon).test {
            assertEquals(Resource.Loading, awaitItem())
            assertEquals(Resource.Success(mockWeather), awaitItem())
            awaitComplete()
        }
    }
}
