package com.irajnajafi1988gmail.notepad.ui.components.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun SettingRow(
    iconRes: Int,
    titleRes: Int,
    expanded: Boolean = false,
    onClick: () -> Unit
) {
    // Animate arrow rotation based on expanded state
    val rotation by animateFloatAsState(targetValue = if (expanded) 90f else 0f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() } // Toggle expansion on click
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Display setting title
        Text(
            text = stringResource(id = titleRes),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f) // Take up remaining space
        )

        // Arrow icon indicating expand/collapse
        Icon(
            painter = painterResource(id = com.irajnajafi1988gmail.notepad.R.drawable.arrow_right),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .size(18.dp)
                .graphicsLayer { rotationZ = rotation }
        )
    }
}
