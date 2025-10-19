package com.irajnajafi1988gmail.notepad.ui.theme

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.irajnajafi1988gmail.notepad.domain.model.ItemDarkMode
import com.irajnajafi1988gmail.notepad.ui.theme.typography.LocalTypography
import com.irajnajafi1988gmail.notepad.ui.theme.typography.buildTypography

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onBackground = Color(0xFFE0E0E0),
    onSurface = Color(0xFFE0E0E0)
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

@Composable
fun NotepadTheme(
    darkMode: ItemDarkMode = ItemDarkMode.SYSTEM,
    dynamicColor: Boolean = true,
    fontSizeScale: Float = 1f,
    content: @Composable () -> Unit
) {
    val isDarkMode = when (darkMode) {
        ItemDarkMode.DARK -> true
        ItemDarkMode.LIGHT -> false
        ItemDarkMode.SYSTEM -> isSystemInDarkTheme()
    }

    val baseColors = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDarkMode) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        isDarkMode -> DarkColorScheme
        else -> LightColorScheme
    }

    val animatedColors = baseColors.copy(
        primary = animateColorAsState(baseColors.primary).value,
        onPrimary = animateColorAsState(baseColors.onPrimary).value,
        secondary = animateColorAsState(baseColors.secondary).value,
        onSecondary = animateColorAsState(baseColors.onSecondary).value,
        background = animateColorAsState(baseColors.background).value,
        onBackground = animateColorAsState(baseColors.onBackground).value,
        surface = animateColorAsState(baseColors.surface).value,
        onSurface = animateColorAsState(baseColors.onSurface).value
    )

    val dynamicTypography = remember(fontSizeScale) { buildTypography(fontSizeScale) }

    CompositionLocalProvider(LocalTypography provides dynamicTypography) {
        MaterialTheme(
            colorScheme = animatedColors,
            typography = dynamicTypography,
            content = content
        )
    }
}
