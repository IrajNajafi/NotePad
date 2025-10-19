package com.irajnajafi1988gmail.notepad.ui.theme.helpers

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.irajnajafi1988gmail.notepad.ui.theme.typography.LocalTypography

/**
 * Computes the font scaling factor based on the app's current typography settings.
 * This allows dynamic scaling of icons, padding, and other UI elements proportionally
 * to the font size chosen by the user or set in the theme.
 *
 * @return Float scale factor relative to a base font size (14sp for bodyMedium).
 */
@Composable
fun fontScale(): Float {
    val baseFont = 14f // Reference font size for bodyMedium
    return LocalTypography.current.bodyMedium.fontSize.value / baseFont
}

/**
 * Returns a scaled icon size based on the font scale.
 *
 * @param base The default icon size in dp (default 24.dp)
 * @return Scaled Dp value
 */
@Composable
fun iconSize(base: Dp = 24.dp): Dp = (base.value * fontScale()).dp

/**
 * Returns a scaled padding value based on the font scale.
 *
 * @param base The default padding in dp (default 16.dp)
 * @return Scaled Dp value
 */
@Composable
fun scaledPadding(base: Dp = 16.dp): Dp = (base.value * fontScale()).dp

/**
 * Returns a scaled button height based on the font scale.
 *
 * @param base The default button height in dp (default 48.dp)
 * @return Scaled Dp value
 */
@Composable
fun buttonHeight(base: Dp = 48.dp): Dp = (base.value * fontScale()).dp

/**
 * Returns a scaled spacing between items based on the font scale.
 *
 * @param base The default spacing in dp (default 12.dp)
 * @return Scaled Dp value
 */
@Composable
fun itemSpacing(base: Dp = 12.dp): Dp = (base.value * fontScale()).dp

/**
 * Returns a scaled corner radius for UI elements based on the font scale.
 *
 * @param base The default corner radius in dp (default 12.dp)
 * @return Scaled Dp value
 */
@Composable
fun cornerRadius(base: Dp = 12.dp): Dp = (base.value * fontScale()).dp

/**
 * Returns a scaled text size based on the font scale.
 *
 * @param base The default TextUnit size
 * @return Scaled TextUnit
 */
@Composable
fun scaledTextSize(base: TextUnit): TextUnit = (base.value * fontScale()).sp
