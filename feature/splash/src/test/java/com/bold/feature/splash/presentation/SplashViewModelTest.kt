package com.bold.feature.splash.presentation

import app.cash.turbine.test
import com.bold.feature.splash.domain.usecase.InitializeAppUseCase
import com.bold.feature.splash.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    private lateinit var initializeAppUseCase: InitializeAppUseCase
    private lateinit var viewModel: SplashViewModel

    @Before
    fun setUp() {
        initializeAppUseCase = mockk(relaxed = true)
        coEvery { initializeAppUseCase.invoke() } returns Unit
    }

    @Test
    fun `init should call initializeAppUseCase and navigate to home`() = runTest(testDispatcher) {
        // When
        viewModel = SplashViewModel(initializeAppUseCase)

        // Attach Turbine before advancing time
        viewModel.effect.test {
            advanceUntilIdle()
            val effect = awaitItem()
            assertEquals(SplashEffect.NavigateToHome, effect)
            cancelAndIgnoreRemainingEvents()
        }

        // Verify state is no longer loading
        assertFalse(viewModel.state.value.isLoading)

        // Verify the usecase was called
        coVerify(exactly = 1) { initializeAppUseCase.invoke() }
    }
}
