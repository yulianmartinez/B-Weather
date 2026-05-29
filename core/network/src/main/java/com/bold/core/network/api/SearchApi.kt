package com.bold.core.network.api

import com.bold.core.network.dto.SearchLocationDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("v1/search.json")
    suspend fun searchLocation(
        @Query("q") query: String
    ): List<SearchLocationDto>
}
