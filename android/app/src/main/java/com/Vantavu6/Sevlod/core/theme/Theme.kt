package com.Vantavu6.Sevlod.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = darkColorScheme(
    primary = NeonViolet,
    onPrimary = TextPrimary,
    primaryContainer = SurfaceAlt,
    onPrimaryContainer = TextPrimary,
    secondary = ElectricTeal,
    onSecondary = BgDeep,
    secondaryContainer = SurfaceAlt,
    onSecondaryContainer = ElectricTeal,
    tertiary = HeadlightOrange,
    onTertiary = BgDeep,
    background = BgDeep,
    onBackground = TextPrimary,
    surface = SurfaceBase,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceAlt,
    onSurfaceVariant = TextSecondary,
    outline = TextMuted,
    outlineVariant = VioletHairline,
    error = HeadlightOrange,
    onError = BgDeep
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        content = content
    )
}
