package com.bold.feature.splash.domain.usecase

import kotlinx.coroutines.delay

class InitializeAppUseCase {
    suspend operator fun invoke() {
        delay(2000L) // Simulate some init work
    }
}
