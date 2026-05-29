package com.bold.core.network.api

import com.bold.core.network.dto.WeatherApiResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("v1/forecast.json")
    suspend fun getForecast(
        @Query("q") query: String,
        @Query("days") days: Int = 3,
        @Query("lang") lang: String = "es"
    ): WeatherApiResponseDto
}
