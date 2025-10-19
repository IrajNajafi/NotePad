package com.irajnajafi1988gmail.notepad.ui.theme.typography

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp


fun buildTypography(scale: Float): Typography {
    return Typography(
        displayLarge = TextStyle(fontSize = (57 * scale).sp),
        displayMedium = TextStyle(fontSize = (45 * scale).sp),
        displaySmall = TextStyle(fontSize = (36 * scale).sp),
        headlineLarge = TextStyle(fontSize = (32 * scale).sp),
        headlineMedium = TextStyle(fontSize = (28 * scale).sp),
        headlineSmall = TextStyle(fontSize = (24 * scale).sp),
        titleLarge = TextStyle(fontSize = (22 * scale).sp),
        titleMedium = TextStyle(fontSize = (16 * scale).sp),
        titleSmall = TextStyle(fontSize = (14 * scale).sp),
        bodyLarge = TextStyle(fontSize = (16 * scale).sp),
        bodyMedium = TextStyle(fontSize = (14 * scale).sp),
        bodySmall = TextStyle(fontSize = (12 * scale).sp),
        labelLarge = TextStyle(fontSize = (14 * scale).sp),
        labelMedium = TextStyle(fontSize = (12 * scale).sp),
        labelSmall = TextStyle(fontSize = (11 * scale).sp)
    )
}
