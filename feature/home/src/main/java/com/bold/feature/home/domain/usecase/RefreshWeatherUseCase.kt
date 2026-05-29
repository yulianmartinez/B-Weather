package com.bold.feature.home.domain.usecase

import com.bold.core.common.result.Resource
import com.bold.core.model.weather.Weather
import com.bold.feature.home.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class RefreshWeatherUseCase(
    private val repository: WeatherRepository
) {
    operator fun invoke(lat: Double, lon: Double): Flow<Resource<Weather>> {
        return repository.getWeather(lat, lon)
    }
}
