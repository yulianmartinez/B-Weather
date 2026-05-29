package com.bold.feature.splash.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InitializeAppUseCaseTest {

    private lateinit var useCase: InitializeAppUseCase

    @Before
    fun setUp() {
        useCase = InitializeAppUseCase()
    }

    @Test
    fun `invoke should delay for 2000 milliseconds`() = runTest {
        val startTime = testScheduler.currentTime
        useCase()
        val endTime = testScheduler.currentTime

        assert(endTime - startTime == 2000L)
    }
}
