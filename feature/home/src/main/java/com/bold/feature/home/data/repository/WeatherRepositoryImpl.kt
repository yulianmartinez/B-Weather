package com.bold.feature.home.data.repository

import com.bold.core.common.result.Resource
import com.bold.core.model.weather.Weather
import com.bold.core.network.api.WeatherApi

import com.bold.feature.home.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import com.bold.core.network.api.SearchApi
import com.bold.core.model.location.LocationData
import com.bold.core.network.dto.SearchLocationDto
import com.bold.core.network.dto.WeatherApiResponseDto

import com.bold.core.model.settings.SettingsRepository
import com.bold.core.model.weather.CurrentWeather
import com.bold.core.model.weather.ForecastDay
import com.bold.core.model.weather.WeatherCondition
import kotlinx.coroutines.flow.first

class WeatherRepositoryImpl(
    private val weatherApi: WeatherApi,
    private val searchApi: SearchApi,
    private val settingsRepository: SettingsRepository
) : WeatherRepository {
    
    override fun getWeather(lat: Double, lon: Double): Flow<Resource<Weather>> = flow {
        emit(Resource.Loading)
        try {
            val lang = settingsRepository.getUserSettings().first().language
            val response = weatherApi.getForecast(
                query = "$lat,$lon",
                lang = lang
            )
            val domainModel = response.toDomain()
            emit(Resource.Success(domainModel))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error", e))
        }
    }

    override fun searchLocations(query: String): Flow<Resource<List<LocationData>>> = flow {
        emit(Resource.Loading)
        try {
            val response = searchApi.searchLocation(query)
            val locations = response.map { it.toDomain() }
            emit(Resource.Success(locations))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error searching locations", e))
        }
    }
}

fun SearchLocationDto.toDomain(): LocationData {
    return LocationData(
        name = name,
        country = country,
        lat = lat,
        lon = lon
    )
}

fun WeatherApiResponseDto.toDomain(): Weather {
    val locationData = LocationData(
        name = location.name,
        country = location.country,
        lat = location.lat,
        lon = location.lon
    )
    
    val currentData = CurrentWeather(
        tempC = current.tempC,
        condition = WeatherCondition(
            text = current.condition.text,
            iconUrl = current.condition.icon,
            code = current.condition.code
        ),
        humidity = current.humidity,
        windKph = current.windKph,
        feelsLikeC = current.feelsLikeC,
        uv = current.uv,
        visibilityKm = current.visKm
    )
    
    val forecastList = forecast.forecastday.map { dayDto ->
        ForecastDay(
            date = dayDto.date,
            dateEpoch = dayDto.dateEpoch,
            avgTempC = dayDto.day.avgTempC,
            minTempC = dayDto.day.minTempC,
            maxTempC = dayDto.day.maxTempC,
            condition = WeatherCondition(
                text = dayDto.day.condition.text,
                iconUrl = dayDto.day.condition.icon,
                code = dayDto.day.condition.code
            )
        )
    }
    
    return Weather(
        location = locationData,
        current = currentData,
        forecast = forecastList
    )
}
