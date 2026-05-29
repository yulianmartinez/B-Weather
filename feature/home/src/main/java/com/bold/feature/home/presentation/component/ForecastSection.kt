package com.bold.feature.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bold.core.model.weather.ForecastDay

import com.bold.core.common.util.TemperatureUtils
import com.bold.core.designsystem.component.BWeatherCard
import com.bold.core.designsystem.component.BWeatherImage
import com.bold.core.designsystem.component.BWeatherSubtitleText
import com.bold.core.designsystem.component.BWeatherTitleText

@Composable
fun ForecastSection(
    forecast: List<ForecastDay>,
    useCelsius: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        BWeatherTitleText(
            text = stringResource(com.bold.core.designsystem.R.string.upcoming_days),
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(forecast) { day ->
                ForecastCard(day, useCelsius)
            }
        }
    }
}

@Composable
fun ForecastCard(day: ForecastDay, useCelsius: Boolean) {
    BWeatherCard {
        Column(
            modifier = Modifier
                .width(100.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BWeatherSubtitleText(text = day.date)

            Spacer(modifier = Modifier.height(16.dp))

            BWeatherImage(
                url = day.condition.iconUrl,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                BWeatherTitleText(
                    text = TemperatureUtils.formatTemperature(day.avgTempC, useCelsius)
                )
            }
        }
    }
}
