package com.irajnajafi1988gmail.notepad.ui.components.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingSubRow(
    title: String,                // Text title for this setting row
    imageRes: Int = 0,            // Optional icon on the left
    trailingText: String? = null, // Optional text displayed on the right
    imageCheckItem: Int? = null,  // Optional checkmark icon for selected state
    isChecked: Boolean = false,   // Whether the row is currently checked
    onClick: () -> Unit,          // Callback when row is clicked
    iconSizeDp: Dp = 20.dp,       // Size for icons
    textSizeSp: TextUnit = 15.sp  // Font size for text
) {
    // Track the pressed state of the row for visual feedback
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value

    // Animate background color based on pressed state
    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed)
            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) // Highlight color when pressed
        else
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f) // Default background color
    )

    // Use Surface as a clickable container
    Surface(
        modifier = Modifier
            .fillMaxWidth()        // Take full width
            .padding(vertical = 4.dp)
            .clickable(
                interactionSource = interactionSource, // Use our custom interaction source
                indication = null                       // No ripple, we handle highlight ourselves
            ) { onClick() },                            // Trigger the provided callback
        color = Color.Transparent                       // Surface itself is transparent, bg handled inside
    ) {
        // Row to layout the icon, title, optional trailing text, and optional checkmark
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(8.dp)) // Rounded highlight background
                .padding(vertical = 10.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left icon if provided, otherwise keep spacing
            if (imageRes != 0) {
                Image(
                    painter = painterResource(imageRes),
                    contentDescription = null,
                    modifier = Modifier.size(iconSizeDp)
                )
            } else {
                Spacer(modifier = Modifier.width(iconSizeDp)) // Reserve space for alignment
            }

            Spacer(modifier = Modifier.width(12.dp)) // Space between icon and title

            // Title text
            Text(
                text = title,
                fontSize = textSizeSp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f) // Take remaining horizontal space
            )

            // Optional trailing text on the right
            if (trailingText != null) {
                Text(
                    text = trailingText,
                    fontSize = textSizeSp,
                    modifier = Modifier
                )
            }

            // Optional checkmark icon if provided
            if (imageCheckItem != null) {
                Spacer(modifier = Modifier.width(8.dp))
                if (isChecked) {
                    Image(
                        painter = painterResource(imageCheckItem),
                        contentDescription = "Checked",
                        modifier = Modifier.size(iconSizeDp)
                    )
                } else {
                    Spacer(modifier = Modifier.size(iconSizeDp)) // Keep layout consistent even when unchecked
                }
            }
        }
    }
}
