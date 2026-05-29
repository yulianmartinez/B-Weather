package com.bold.feature.home.domain.usecase

import app.cash.turbine.test
import com.bold.core.common.result.Resource
import com.bold.core.model.location.LocationData
import com.bold.feature.home.domain.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchLocationsUseCaseTest {

    private lateinit var repository: WeatherRepository
    private lateinit var useCase: SearchLocationsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = SearchLocationsUseCase(repository)
    }

    @Test
    fun `invoke should return repository search flow`() = runTest {
        // Given
        val query = "Bogota"
        val mockLocations = listOf(
            LocationData("Bogota", "Colombia", 4.6097, -74.0817)
        )
        
        coEvery { repository.searchLocations(query) } returns flowOf(
            Resource.Loading,
            Resource.Success(mockLocations)
        )

        // When & Then
        useCase(query).test {
            assertEquals(Resource.Loading, awaitItem())
            assertEquals(Resource.Success(mockLocations), awaitItem())
            awaitComplete()
        }
    }
}
