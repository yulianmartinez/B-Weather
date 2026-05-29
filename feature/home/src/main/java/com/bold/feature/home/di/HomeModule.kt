package com.bold.feature.home.di

import com.bold.feature.home.data.repository.WeatherRepositoryImpl
import com.bold.feature.home.domain.repository.WeatherRepository
import com.bold.feature.home.domain.usecase.GetCurrentWeatherUseCase
import com.bold.feature.home.domain.usecase.RefreshWeatherUseCase
import com.bold.feature.home.presentation.viewmodel.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

import com.bold.feature.home.domain.usecase.SearchLocationsUseCase

val homeModule = module {
    single<WeatherRepository> { WeatherRepositoryImpl(get(), get(), get()) }
    factory { GetCurrentWeatherUseCase(get()) }
    factory { RefreshWeatherUseCase(get()) }
    factory { SearchLocationsUseCase(get()) }
    viewModelOf(::HomeViewModel)
}
