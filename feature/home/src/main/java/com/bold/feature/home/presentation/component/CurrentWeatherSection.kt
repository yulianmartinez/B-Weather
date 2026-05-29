package com.bold.feature.home.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bold.core.designsystem.icon.WeatherIcons
import com.bold.core.model.weather.Weather

import com.bold.core.common.util.TemperatureUtils
import com.bold.core.common.util.TemperatureUtils.formatTemperature
import com.bold.core.designsystem.component.BWeatherCard
import com.bold.core.designsystem.component.BWeatherHeadlineText
import com.bold.core.designsystem.component.BWeatherImage
import com.bold.core.designsystem.component.BWeatherLabelText
import com.bold.core.designsystem.component.BWeatherSubtitleText
import com.bold.core.designsystem.component.BWeatherTitleText
import com.bold.core.designsystem.component.BWeatherValueText
import com.bold.core.designsystem.theme.Black

@Composable
fun CurrentWeatherSection(
    weather: Weather,
    useCelsius: Boolean,
    modifier: Modifier = Modifier
) {
    BWeatherCard(
        modifier = modifier.padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val todayForecast = weather.forecast.firstOrNull()
            val maxTempStr = todayForecast?.let { formatTemperature(it.maxTempC, useCelsius) } ?: "--"
            val minTempStr = todayForecast?.let { formatTemperature(it.minTempC, useCelsius) } ?: "--"

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = WeatherIcons.Location,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                
                BWeatherTitleText(text = weather.location.name)
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    DetailItem(
                        label = stringResource(com.bold.core.designsystem.R.string.min_temp),
                        value = minTempStr
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    DetailItem(
                        label = stringResource(com.bold.core.designsystem.R.string.max_temp),
                        value = maxTempStr
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                BWeatherImage(
                    url = weather.current.condition.iconUrl,
                    contentDescription = weather.current.condition.text,
                    modifier = Modifier.size(45.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                BWeatherHeadlineText(
                    text = formatTemperature(weather.current.tempC, useCelsius)
                )
            }

            BWeatherSubtitleText(text = weather.current.condition.text)

            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DetailItem(
                    icon = WeatherIcons.Humidity,
                    label = stringResource(com.bold.core.designsystem.R.string.humidity),
                    value = "${weather.current.humidity}%"
                )
                DetailItem(
                    icon = WeatherIcons.Wind,
                    label = stringResource(com.bold.core.designsystem.R.string.wind),
                    value = "${weather.current.windKph} km/h"
                )
                DetailItem(
                    icon = WeatherIcons.Thermostat,
                    label = stringResource(com.bold.core.designsystem.R.string.feels_like),
                    value = formatTemperature(weather.current.feelsLikeC, useCelsius)
                )
                DetailItem(
                    icon = WeatherIcons.Visibility,
                    label = stringResource(com.bold.core.designsystem.R.string.visibility),
                    value = "${weather.current.visibilityKm} km"
                )
            }
        }
    }
}

@Composable
fun DetailItem(
    icon: ImageVector? = null,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        icon?.let { safeIcon ->
            Icon(
                imageVector = safeIcon,
                contentDescription = label,
                tint = Black,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        BWeatherValueText(text = value)
        Spacer(modifier = Modifier.height(4.dp))
        BWeatherLabelText(text = label)
    }
}
