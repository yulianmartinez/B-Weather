package com.bold.feature.home.presentation.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import com.bold.core.designsystem.icon.WeatherIcons
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.bold.core.designsystem.R
import com.bold.core.designsystem.component.BWeatherLoading
import com.bold.core.model.weather.Weather
import com.bold.feature.home.presentation.component.CurrentWeatherSection
import com.bold.feature.home.presentation.component.ForecastSection
import com.bold.feature.home.presentation.state.HomeIntent
import com.bold.feature.home.presentation.viewmodel.HomeViewModel
import com.bold.feature.home.presentation.component.WeatherHeader
import com.bold.feature.home.presentation.state.HomeState


@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToSettings: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    
    HomeScreenContent(
        state = state,
        onIntent = viewModel::handleIntent,
        onNavigateToSettings = onNavigateToSettings
    )
}

@Composable
fun HomeScreenContent(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .systemBarsPadding()
    ) {
        BWeatherLoading(
            isVisible = state.isLoading
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            WeatherHeader(
                searchQuery = state.searchQuery,
                isSearching = state.isSearching,
                searchResults = state.searchResults,
                onQueryChange = { onIntent(HomeIntent.UpdateSearchQuery(it)) },
                onClearSearch = { onIntent(HomeIntent.ClearSearch) },
                onLocationSelected = { location ->
                    onIntent(HomeIntent.SelectLocation(location.lat, location.lon))
                },
                onNavigateToSettings = onNavigateToSettings
            )

            Box(modifier = Modifier.fillMaxSize().weight(1f)) {
                if (state.error != null && state.weather == null) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error: ${state.error}",
                            color = colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { onIntent(HomeIntent.LoadWeather) }) {
                            Text(stringResource(com.bold.core.designsystem.R.string.retry))
                        }
                    }
                }

                state.weather?.let { weather ->
                    val configuration = LocalConfiguration.current
                    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

                    if (isLandscape) {
                        ContentLandscape(weather, state)
                    } else {
                        ContentPortrait(weather, state)
                    }
                } ?: run {
                    ContentEmpty()
                }
            }
        }
    }
}

@Composable
fun ContentLandscape(
    weather: Weather,
    state: HomeState
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 8.dp)
    ) {
        CurrentWeatherSection(
            weather = weather,
            useCelsius = state.useCelsius,
            Modifier
                .weight(1f)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        )

        Spacer(modifier = Modifier.width(16.dp))

        ForecastSection(
            forecast = weather.forecast,
            useCelsius = state.useCelsius,
            modifier = Modifier
                .weight(1f)
                .padding(top = 24.dp)
                .fillMaxSize(),
        )
    }
}

@Composable
fun ContentPortrait(
    weather: Weather,
    state: HomeState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp)
    ) {
        CurrentWeatherSection(
            weather = weather,
            useCelsius = state.useCelsius
        )

        Spacer(modifier = Modifier.height(8.dp))

        ForecastSection(
            forecast = weather.forecast,
            useCelsius = state.useCelsius
        )
    }
}

@Composable
private fun BoxScope.ContentEmpty() {
    Column(
        modifier = Modifier.align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = WeatherIcons.Search,
            contentDescription = stringResource(R.string.search_hint),
            tint = colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = stringResource(R.string.search_city_prompt),
            style = MaterialTheme.typography.bodyLarge,
            color = colorScheme.onSurfaceVariant
        )
    }
}