package com.googledevteam.bookfinder.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF315D43),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD2E8D4),
    onPrimaryContainer = Color(0xFF0D2113),
    secondary = Color(0xFF705C2E),
    background = Color(0xFFF7F7F1),
    surface = Color(0xFFF7F7F1),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFB6D8BA),
    onPrimary = Color(0xFF1C3825),
    primaryContainer = Color(0xFF315D43),
    onPrimaryContainer = Color(0xFFD2E8D4),
    secondary = Color(0xFFE2C78C),
)

@Composable
fun BookFinderTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        typography = MaterialTheme.typography,
        content = content,
    )
}
