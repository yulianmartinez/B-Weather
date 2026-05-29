package com.ymd.bweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.bold.core.designsystem.theme.BWeatherTheme
import com.ymd.bweather.ui.BWeatherApp

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.bold.core.model.settings.SettingsRepository
import com.bold.core.model.settings.UserSettings
import org.koin.android.ext.android.inject
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val settingsRepository: SettingsRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settings by settingsRepository.getUserSettings().collectAsState(
                initial = UserSettings()
            )
            val locale = Locale(settings.language)
            val configuration = LocalConfiguration.current
            configuration.setLocale(locale)
            val context = LocalContext.current
            val updatedContext = context.createConfigurationContext(configuration)

            CompositionLocalProvider(
                LocalContext provides updatedContext,
                LocalConfiguration provides configuration
            ) {
                BWeatherTheme {
                    BWeatherApp()
                }
            }
        }
    }
}