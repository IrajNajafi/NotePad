package com.irajnajafi1988gmail.notepad.ui.components.common.cardCommon

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.irajnajafi1988gmail.notepad.R
import com.irajnajafi1988gmail.notepad.ui.theme.gold
import com.irajnajafi1988gmail.notepad.ui.theme.helpers.iconSize

/**
 * A reusable composable for displaying cards for both notes and checklists.
 * Handles favorite, trash, selection, and restore functionality.
 *
 * @param title The title of the item
 * @param content The content/description of the item
 * @param isFavorite Whether the item is marked as favorite
 * @param isTrashed Whether the item is in trash (affects UI)
 * @param isSelected Whether the item is selected (for multi-selection mode)
 * @param showCheckbox Whether to show a selection checkbox
 * @param displayDate Formatted date string to display
 * @param onClick Callback when the card is clicked
 * @param onLongClick Callback when the card is long-pressed
 * @param onFavoriteClick Callback when favorite icon is clicked
 * @param onRestoreClick Optional callback for restoring item from trash
 * @param onCheckChange Callback when checkbox selection changes
 * @param emptyTitleRes Resource ID for fallback text if title/content is empty
 * @param modifier Modifier for styling/layout adjustments
 */
@Composable
fun ItemCardCommon(
    title: String,
    content: String,
    isFavorite: Boolean,
    isTrashed: Boolean = false,
    isSelected: Boolean = false,
    showCheckbox: Boolean = false,
    displayDate: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onRestoreClick: (() -> Unit)? = null,
    onCheckChange: (Boolean) -> Unit = {},
    emptyTitleRes: Int,
    modifier: Modifier
) {
    // Animate the background color based on selection state
    val animatedContainerColor by animateColorAsState(
        targetValue = if (isSelected)
            MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
        else
            MaterialTheme.colorScheme.surfaceContainerLow,
        label = "cardColorAnim"
    )

    // Card container
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp) // Fixed height for uniform cards
            .pointerInput(Unit) {
                // Detect tap and long-press gestures
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = { onLongClick() }
                )
            },
        shape = RoundedCornerShape(12.dp), // Rounded corners
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = animatedContainerColor)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {

                // Display timestamp
                Text(
                    text = displayDate,
                    fontSize = 10.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 5.dp, start = 5.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Top row: favorite/restore icons and optional checkbox
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (!isTrashed) {
                            // Favorite button for non-trashed items
                            IconButton(onClick = onFavoriteClick) {
                                val iconRes = if (isFavorite)
                                    R.drawable.light_star_icon
                                else
                                    R.drawable.favorit

                                val tintColor = if (isFavorite) gold else LocalContentColor.current

                                Image(
                                    painter = painterResource(iconRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(iconSize()),
                                    colorFilter = ColorFilter.tint(tintColor)
                                )
                            }
                        } else {
                            // Restore button for trashed items
                            IconButton(onClick = { onRestoreClick?.invoke() }) {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_refresh_24),
                                    contentDescription = null,
                                    modifier = Modifier.size(iconSize()),
                                    tint = Color.Blue
                                )
                            }
                        }
                    }

                    // Checkbox for selection mode
                    if (showCheckbox) {
                        IconButton(onClick = { onCheckChange(!isSelected) }) {
                            val iconRes = if (isSelected)
                                R.drawable.checkbox_full_tick_icon
                            else
                                R.drawable.blue_checkbox_empty_icon
                            Image(
                                painter = painterResource(id = iconRes),
                                contentDescription = null,
                                modifier = Modifier.size(iconSize())
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Display title/content text
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when {
                            title.isNotEmpty() -> title
                            content.isNotEmpty() -> content.take(20) + "..."
                            else -> stringResource(id = emptyTitleRes)
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
