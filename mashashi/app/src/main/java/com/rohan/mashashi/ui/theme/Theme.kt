package com.rohan.mashashi.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = MashashiColors.primary,
    primaryVariant = MashashiColors.primaryVariant,
    secondary = MashashiColors.secondary,
    secondaryVariant = MashashiColors.secondaryVariant,
    tertiary = MashashiColors.tertiary,
    tertiaryVariant = MashashiColors.tertiaryVariant,
    error = MashashiColors.error,
    surface = MashashiColors.surface,
    surfaceVariant = MashashiColors.surfaceVariant,
    background = MashashiColors.background,
    onPrimary = MashashiColors.onPrimary,
    onSecondary = MashashiColors.onSecondary,
    onTertiary = MashashiColors.onTertiary,
    onError = MashashiColors.onError,
    onSurface = MashashiColors.onSurface,
    onBackground = MashashiColors.onBackground
)

private val LightColorScheme = lightColorScheme(
    primary = MashashiColors.primary,
    primaryVariant = MashashiColors.primaryVariant,
    secondary = MashashiColors.secondary,
    secondaryVariant = MashashiColors.secondaryVariant,
    tertiary = MashashiColors.tertiary,
    tertiaryVariant = MashashiColors.tertiaryVariant,
    error = MashashiColors.error,
    surface = MashashiColors.surface,
    surfaceVariant = MashashiColors.surfaceVariant,
    background = MashashiColors.background,
    onPrimary = MashashiColors.onPrimary,
    onSecondary = MashashiColors.onSecondary,
    onTertiary = MashashiColors.onTertiary,
    onError = MashashiColors.onError,
    onSurface = MashashiColors.onSurface,
    onBackground = MashashiColors.onBackground
)

@Composable
fun MashashiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = MashashiTypography,
        content = content
    )
}