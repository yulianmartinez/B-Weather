package com.bold.feature.home.presentation.viewmodel

import app.cash.turbine.test
import com.bold.core.common.result.Resource
import com.bold.core.model.location.LocationData
import com.bold.core.model.settings.SettingsRepository
import com.bold.core.model.settings.UserSettings
import com.bold.core.model.weather.CurrentWeather
import com.bold.core.model.weather.Weather
import com.bold.core.model.weather.WeatherCondition
import com.bold.feature.home.domain.usecase.GetCurrentWeatherUseCase
import com.bold.feature.home.domain.usecase.SearchLocationsUseCase
import com.bold.feature.home.presentation.state.HomeIntent
import com.bold.feature.home.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getCurrentWeatherUseCase: GetCurrentWeatherUseCase
    private lateinit var searchLocationsUseCase: SearchLocationsUseCase
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        getCurrentWeatherUseCase = mockk()
        searchLocationsUseCase = mockk()
        settingsRepository = mockk(relaxed = true)

        coEvery { settingsRepository.getUserSettings() } returns flowOf(UserSettings())
        coEvery { settingsRepository.saveLastLocation(any(), any()) } returns Unit
    }

    @Test
    fun `when init with last location, should fetch weather automatically`() = runTest {
        // Given
        val lat = 4.6
        val lon = -74.0
        coEvery { settingsRepository.getUserSettings() } returns flowOf(
            UserSettings(lastLat = lat, lastLon = lon)
        )
        
        val mockWeather = Weather(
            location = LocationData("Bogota", "Colombia", lat, lon),
            current = CurrentWeather(20.0, WeatherCondition("Sunny", "", 1000), 50, 10.0, 22.0, 5.0, 10.0),
            forecast = emptyList()
        )
        
        coEvery { getCurrentWeatherUseCase(lat, lon) } returns flowOf(
            Resource.Loading,
            Resource.Success(mockWeather)
        )
        
        // When
        viewModel = HomeViewModel(
            getCurrentWeatherUseCase,
            searchLocationsUseCase,
            settingsRepository
        )

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertNotNull(state.weather)
            assertEquals("Bogota", state.weather?.location?.name)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `SelectLocation intent should update state and fetch weather`() = runTest {
        // Given
        viewModel = HomeViewModel(
            getCurrentWeatherUseCase,
            searchLocationsUseCase,
            settingsRepository
        )
        
        val lat = 4.6
        val lon = -74.0
        
        val mockWeather = Weather(
            location = LocationData("Bogota", "Colombia", lat, lon),
            current = CurrentWeather(20.0, WeatherCondition("Sunny", "", 1000), 50, 10.0, 22.0, 5.0, 10.0),
            forecast = emptyList()
        )
        
        coEvery { getCurrentWeatherUseCase(lat, lon) } returns flowOf(
            Resource.Loading,
            Resource.Success(mockWeather)
        )
        
        // Then
        viewModel.state.test {
            viewModel.handleIntent(HomeIntent.SelectLocation(lat, lon))
            
            val finalState = expectMostRecentItem()
            assertFalse(finalState.isLoading)
            assertNotNull(finalState.weather)
            assertEquals("Bogota", finalState.weather?.location?.name)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `UpdateSearchQuery intent should debounce and emit results`() = runTest {
        // Given
        viewModel = HomeViewModel(
            getCurrentWeatherUseCase,
            searchLocationsUseCase,
            settingsRepository
        )
        
        val mockLocations = listOf(
            LocationData("Bogota", "Colombia", 4.6, -74.0)
        )
        
        coEvery { searchLocationsUseCase("Bog") } returns flowOf(
            Resource.Loading,
            Resource.Success(mockLocations)
        )
        
        // When
        viewModel.state.test {
            viewModel.handleIntent(HomeIntent.UpdateSearchQuery("B"))
            viewModel.handleIntent(HomeIntent.UpdateSearchQuery("Bo"))
            viewModel.handleIntent(HomeIntent.UpdateSearchQuery("Bog"))
            
            // Advance time past the 500ms debounce
            testScheduler.advanceTimeBy(600)
            
            val finalState = expectMostRecentItem()
            assertFalse(finalState.isSearching)
            assertEquals(1, finalState.searchResults.size)
            assertEquals("Bogota", finalState.searchResults[0].name)
            assertEquals("Bog", finalState.searchQuery)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getWeather error should update state with error`() = runTest {
        // Given
        val lat = 4.6
        val lon = -74.0
        coEvery { settingsRepository.getUserSettings() } returns flowOf(
            UserSettings(lastLat = lat, lastLon = lon)
        )
        
        coEvery { getCurrentWeatherUseCase(lat, lon) } returns flowOf(
            Resource.Loading,
            Resource.Error("API failed")
        )
        
        // When
        viewModel = HomeViewModel(
            getCurrentWeatherUseCase,
            searchLocationsUseCase,
            settingsRepository
        )

        // Then
        viewModel.state.test {
            val state = expectMostRecentItem()
            assertFalse(state.isLoading)
            assertNotNull(state.error)
            assertEquals("API failed", state.error)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `SelectLocation should clear search results and query`() = runTest {
        // Given
        viewModel = HomeViewModel(
            getCurrentWeatherUseCase,
            searchLocationsUseCase,
            settingsRepository
        )
        
        val lat = 4.6
        val lon = -74.0
        
        val mockWeather = Weather(
            location = LocationData("Bogota", "Colombia", lat, lon),
            current = CurrentWeather(20.0, WeatherCondition("Sunny", "", 1000), 50, 10.0, 22.0, 5.0, 10.0),
            forecast = emptyList()
        )
        
        coEvery { getCurrentWeatherUseCase(lat, lon) } returns flowOf(
            Resource.Success(mockWeather)
        )
        coEvery { searchLocationsUseCase(any()) } returns flowOf(Resource.Success(emptyList()))
        coEvery { settingsRepository.saveLastLocation(any(), any()) } returns Unit
        
        // When
        viewModel.state.test {
            viewModel.handleIntent(HomeIntent.UpdateSearchQuery("Bogota"))
            
            // Then select location
            viewModel.handleIntent(HomeIntent.SelectLocation(lat, lon))
            
            val finalState = expectMostRecentItem()
            assertEquals("", finalState.searchQuery)
            assertTrue(finalState.searchResults.isEmpty())
            
            cancelAndIgnoreRemainingEvents()
        }
    }
}
