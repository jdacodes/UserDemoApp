package com.jdacodes.userdemo.core.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BaseLight,
    secondary = LavenderLight,
    tertiary = MauveLight,
    background = CrustDark

)

private val LightColorScheme = lightColorScheme(
    primary = BaseDark,
    secondary = LavenderDark,
    tertiary = MauveDark,
    background = CrustLight

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun UserDemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
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
        shapes = shapes,
        content = content
    )
}
@Composable
fun getAppBarColor(): Color {
    return if (isSystemInDarkTheme()) {
        BaseDark
    } else {
        BaseLight
    }
}
// Function to get gradient background
@Composable
fun getGradientBackground(): Brush {
    return if (isSystemInDarkTheme()) {
        Brush.linearGradient(colors = DarkGradientColors)
    } else {
        Brush.linearGradient(colors = LightGradientColors)
    }
}

// Function to get text color based on the theme
@Composable
fun getTextColor(): androidx.compose.ui.graphics.Color {
    return if (isSystemInDarkTheme()) {
        DarkTextColor
    } else {
        LightTextColor
    }
}