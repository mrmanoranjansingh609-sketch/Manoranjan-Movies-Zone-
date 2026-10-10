package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val ManoranjanDarkColorScheme = darkColorScheme(
    primary = NeonPurplePrimary,
    onPrimary = TextPrimary,
    primaryContainer = NeonPurpleDark,
    onPrimaryContainer = TextPrimary,
    secondary = NeonCyan,
    onSecondary = ManoranjanDarkBg,
    secondaryContainer = ManoranjanSurfaceVariant,
    onSecondaryContainer = TextPrimary,
    tertiary = NeonPink,
    onTertiary = TextPrimary,
    background = ManoranjanDarkBg,
    onBackground = TextPrimary,
    surface = ManoranjanSurface,
    onSurface = TextPrimary,
    surfaceVariant = ManoranjanSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = ManoranjanBorder,
    outlineVariant = ManoranjanBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to stunning Manoranjan cinema dark mode
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = ManoranjanDarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = ManoranjanDarkBg.toArgb()
            window.navigationBarColor = ManoranjanDarkBg.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
