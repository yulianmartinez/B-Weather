package com.bold.feature.home.domain.usecase

import com.bold.core.common.result.Resource
import com.bold.core.model.location.LocationData
import com.bold.feature.home.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class SearchLocationsUseCase(
    private val weatherRepository: WeatherRepository
) {
    operator fun invoke(query: String): Flow<Resource<List<LocationData>>> {
        return weatherRepository.searchLocations(query)
    }
}
