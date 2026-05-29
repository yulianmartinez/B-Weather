package com.bold.feature.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bold.core.designsystem.icon.WeatherIcons
import com.bold.core.model.location.LocationData

@Composable
fun WeatherHeader(
    searchQuery: String,
    isSearching: Boolean,
    searchResults: List<LocationData>,
    onQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onLocationSelected: (LocationData) -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        colorScheme.secondary,
                        colorScheme.primary,
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "B-W",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = colorScheme.onPrimary,
                    modifier = Modifier.padding(top = 14.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                SearchBarComponent(
                    query = searchQuery,
                    onQueryChange = onQueryChange,
                    onClearSearch = onClearSearch,
                    isSearching = isSearching,
                    searchResults = searchResults,
                    onLocationSelected = onLocationSelected,
                    modifier = Modifier.weight(1f)
                )
                
                Spacer(modifier = Modifier.width(4.dp))
                
                IconButton(
                    onClick = onNavigateToSettings,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = WeatherIcons.Settings,
                        contentDescription = stringResource(com.bold.core.designsystem.R.string.settings),
                        tint = colorScheme.onPrimary
                    )
                }
            }
        }
    }
}
