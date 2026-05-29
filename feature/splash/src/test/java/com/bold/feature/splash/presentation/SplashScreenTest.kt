package com.bold.feature.splash.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class SplashScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun splashScreen_displaysLogoAndAppName() {
        composeTestRule.setContent {
            SplashScreenContent()
        }

        // Verify the app name is shown
        composeTestRule.onNodeWithText("B-Weather").assertIsDisplayed()

        // Verify the cloud icon is shown
        composeTestRule.onNodeWithContentDescription("App Logo").assertIsDisplayed()
    }

    @Test
    fun splashScreen_handlesNavigateToHomeEffect() = runTest {
        var navigatedToHome = false
        val mockViewModel: SplashViewModel = mockk(relaxed = true)
        val effectFlow = MutableSharedFlow<SplashEffect>()
        val stateFlow = MutableStateFlow(SplashState())

        every { mockViewModel.effect } returns effectFlow
        every { mockViewModel.state } returns stateFlow

        composeTestRule.setContent {
            SplashScreen(
                onNavigateToHome = { navigatedToHome = true },
                viewModel = mockViewModel
            )
        }

        // Emit the effect
        effectFlow.emit(SplashEffect.NavigateToHome)

        // Give coroutines time to process
        composeTestRule.waitForIdle()

        assertTrue(navigatedToHome)
    }
}
