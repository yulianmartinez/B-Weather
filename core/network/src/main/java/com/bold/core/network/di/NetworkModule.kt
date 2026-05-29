package com.bold.core.network.di

import com.bold.core.network.BuildConfig
import com.bold.core.network.api.SearchApi
import com.bold.core.network.api.WeatherApi
import com.bold.core.network.interceptor.ApiKeyInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val networkModule = module {

    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    single {
        get<Retrofit>(named("weatherapi_retrofit")).create(WeatherApi::class.java)
    }

    single(named("weatherapi_client")) {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ApiKeyInterceptor(BuildConfig.WEATHER_API_KEY))
            .build()
    }

    single(named("weatherapi_retrofit")) {
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
        val contentType = "application/json".toMediaType()
        
        Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/")
            .client(get(named("weatherapi_client")))
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    single {
        get<Retrofit>(named("weatherapi_retrofit")).create(SearchApi::class.java)
    }
}
