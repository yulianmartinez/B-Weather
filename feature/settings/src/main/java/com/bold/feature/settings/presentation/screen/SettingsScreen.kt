package com.bold.feature.settings.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.DropdownMenuItem
import com.bold.core.designsystem.icon.WeatherIcons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bold.feature.settings.presentation.state.SettingsIntent
import com.bold.feature.settings.presentation.state.SettingsState
import com.bold.feature.settings.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    SettingsScreenContent(
        state = state,
        onIntent = viewModel::handleIntent,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            Box(
                modifier = Modifier
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
                TopAppBar(
                    title = { Text(stringResource(com.bold.core.designsystem.R.string.settings)) },
                    navigationIcon = {
                        var isBackClicked by remember { mutableStateOf(false) }
                        IconButton(onClick = {
                            if (!isBackClicked) {
                                isBackClicked = true
                                onNavigateBack()
                            }
                        }) {
                            Icon(
                                imageVector = WeatherIcons.Back,
                                contentDescription = "Volver"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = colorScheme.onPrimary,
                        navigationIconContentColor = colorScheme.onPrimary
                    )
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(com.bold.core.designsystem.R.string.settings),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(com.bold.core.designsystem.R.string.temperature_unit),
                style = MaterialTheme.typography.bodyLarge,
                color = colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            var tempExpanded by remember { mutableStateOf(false) }
            val tempUnits = listOf(
                true to "Celsius (°C)",
                false to "Fahrenheit (°F)"
            )

            ExposedDropdownMenuBox(
                expanded = tempExpanded,
                onExpandedChange = { tempExpanded = !tempExpanded }
            ) {
                OutlinedTextField(
                    value = tempUnits.find { it.first == state.userSettings.useCelsius }?.second ?: "",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tempExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = tempExpanded,
                    onDismissRequest = { tempExpanded = false }
                ) {
                    tempUnits.forEach { (isCelsius, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                onIntent(SettingsIntent.UpdateTemperatureUnit(isCelsius))
                                tempExpanded = false
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = stringResource(com.bold.core.designsystem.R.string.language),
                style = MaterialTheme.typography.bodyLarge,
                color = colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            var expanded by remember { mutableStateOf(false) }
            val languages = listOf(
                "es" to "🇪🇸 ${stringResource(com.bold.core.designsystem.R.string.spanish)}",
                "en" to "🇺🇸 ${stringResource(com.bold.core.designsystem.R.string.english)}"
            )
            
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = languages.find { it.first == state.userSettings.language }?.second ?: "",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    languages.forEach { (code, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                onIntent(SettingsIntent.UpdateLanguage(code))
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}
