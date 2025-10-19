package com.irajnajafi1988gmail.notepad.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.irajnajafi1988gmail.notepad.ui.theme.helpers.scaledTextSize
import com.irajnajafi1988gmail.notepad.ui.theme.typography.LocalTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemTabBar(
    title: String = "",
    navigationIcon: Int? = null,
    actionIcon: Int? = null,
    iconSizeDp: Dp = 42.dp,
    onNavigationClick: (() -> Unit)? = null,
    onActionClick: (() -> Unit)? = null,
) {
    val layoutDirection = LocalLayoutDirection.current

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = LocalTypography.current.titleMedium.copy(
                    fontSize = scaledTextSize(LocalTypography.current.titleMedium.fontSize)
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            if (navigationIcon != null && onNavigationClick != null) {
                IconButton(onClick = onNavigationClick) {
                    Image(
                        painter = painterResource(id = navigationIcon),
                        contentDescription = "Navigation Icon",
                        modifier = Modifier
                            .size(iconSizeDp)
                            .graphicsLayer(
                                scaleX = if (layoutDirection == LayoutDirection.Rtl) -1f else 1f
                            )
                    )
                }
            }
        },
        actions = {
            if (actionIcon != null && onActionClick != null) {
                IconButton(onClick = onActionClick) {
                    Image(
                        painter = painterResource(id = actionIcon),
                        contentDescription = "Action Icon",
                        modifier = Modifier.size(iconSizeDp)
                    )
                }
            }
        }
    )
}
