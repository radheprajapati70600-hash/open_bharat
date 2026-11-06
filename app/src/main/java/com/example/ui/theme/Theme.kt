package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = SaffronDark,
    onPrimary = Color.White,
    primaryContainer = SaffronContainerLight,
    onPrimaryContainer = OnSaffronContainerLight,
    secondary = IndiaGreen,
    onSecondary = Color.White,
    secondaryContainer = GreenContainerLight,
    onSecondaryContainer = OnGreenContainerLight,
    tertiary = ChakraBlue,
    onTertiary = Color.White,
    background = BackgroundLight,
    onBackground = OnSurfaceLight,
    surface = Color.White,
    onSurface = OnSurfaceLight,
    surfaceVariant = Color(0xFFEDEDED),
    onSurfaceVariant = Color(0xFF444746)
)

private val DarkColorScheme = darkColorScheme(
    primary = SaffronPrimary,
    onPrimary = Color(0xFF4A2000),
    primaryContainer = SaffronContainerDark,
    onPrimaryContainer = OnSaffronContainerDark,
    secondary = Color(0xFF48C73C),
    onSecondary = Color(0xFF023801),
    secondaryContainer = Color(0xFF0A4F05),
    onSecondaryContainer = Color(0xFFB4F5AD),
    tertiary = Color(0xFF82B1FF),
    onTertiary = Color(0xFF002266),
    background = BackgroundDark,
    onBackground = OnSurfaceDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = Color(0xFF2C302D),
    onSurfaceVariant = Color(0xFFC4C7C4)
)

@Composable
fun OpenBharatTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
