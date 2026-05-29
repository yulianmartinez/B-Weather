package com.bold.feature.splash.di

import com.bold.feature.splash.domain.usecase.InitializeAppUseCase
import com.bold.feature.splash.presentation.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val splashModule = module {
    factory { InitializeAppUseCase() }
    viewModel { SplashViewModel(get()) }
}
