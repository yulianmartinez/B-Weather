package com.bold.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage

@Composable
fun BWeatherImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    val fixedUrl = if (url.startsWith("//")) "https:$url" else url
    
    AsyncImage(
        model = fixedUrl,
        contentDescription = contentDescription,
        modifier = modifier
    )
}
