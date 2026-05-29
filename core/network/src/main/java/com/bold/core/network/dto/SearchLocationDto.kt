package com.bold.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class SearchLocationDto(
    val id: Long,
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val url: String
)
