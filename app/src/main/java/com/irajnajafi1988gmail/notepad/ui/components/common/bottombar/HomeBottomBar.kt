package com.irajnajafi1988gmail.notepad.ui.components.common.bottombar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.irajnajafi1988gmail.notepad.domain.model.ItemDarkMode
import com.irajnajafi1988gmail.notepad.domain.utils.noRippleClickable
import com.irajnajafi1988gmail.notepad.ui.theme.helpers.iconSize
import com.irajnajafi1988gmail.notepad.ui.viewModel.DarkModeViewModel

/**
 * ✅ Bottom navigation bar for the Home screen
 * Contains left group (trash, favorite), center button (add note), and right group (dark mode, etc.)
 */
@Composable
fun HomeBottomBar(
    modifier: Modifier = Modifier,
    trashCount: Int,
    favoriteCount: Int,
    onItemClick: (item: ItemsHomeBottomBar) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(bottom = 5.dp)
    ) {
        // 🔹 Main rounded rectangle background for the bottom bar
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            // 🔹 Row contains left and right icon groups
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 🔹 Left group (Trash, Favorite)
                IconGroup(
                    items = ItemsHomeBottomBar.leftItems,
                    onClick = onItemClick,
                    trashCount = trashCount,
                    favoriteCount = favoriteCount
                )

                // 🔹 Right group (e.g., DarkMode, Settings)
                IconGroup(
                    items = ItemsHomeBottomBar.rightItems,
                    onClick = onItemClick
                )
            }
        }

        // 🔹 Floating circular middle button (usually Add Note)
        val middle = ItemsHomeBottomBar.middleItem.first()
        Box(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.BottomCenter)
                .offset(y = (-20).dp) // raise above main bar
                .shadow(elevation = 6.dp, shape = CircleShape, clip = false)
                .noRippleClickable { onItemClick(middle) }
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // 🔹 Icon for middle button
            Image(
                painter = painterResource(id = middle.icon),
                contentDescription = stringResource(id = middle.description),
                modifier = Modifier
                    .padding(8.dp)
                    .size(45.dp)
            )
        }
    }
}

/**
 * 🔹 Represents a group of icons in the bottom bar (left or right side)
 * - Can contain icons with badges (e.g., Trash, Favorite)
 * - Handles Dark Mode icon rotation animation
 */
@Composable
private fun IconGroup(
    items: List<ItemsHomeBottomBar>,
    onClick: (item: ItemsHomeBottomBar) -> Unit,
    trashCount: Int = 0,
    favoriteCount: Int = 0,
    darkModeViewModel: DarkModeViewModel = hiltViewModel(),
) {
    // 🔹 Collect current dark mode value from ViewModel
    val darkMode by darkModeViewModel.selectedDarkMode.collectAsState()

    // 🔹 Animate dark mode icon rotation depending on the mode
    val rotationAngle by animateFloatAsState(
        targetValue = if (darkMode == ItemDarkMode.DARK) -90f else 90f,
        animationSpec = tween(300),
        label = "DarkModeRotation"
    )

    // 🔹 Arrange all icons horizontally
    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            // 🔹 Determine badge count for each item
            val count = when (item) {
                ItemsHomeBottomBar.TRASH -> trashCount
                ItemsHomeBottomBar.FAVORITE -> favoriteCount
                else -> 0
            }

            val showBadge = count > 0

            // 🔹 If badge should be shown (e.g., unread count)
            if (showBadge) {
                BadgeIcon(
                    item = item,
                    count = count,
                    onClick = onClick
                )
            } else {
                // 🔹 Normal icon (no badge)
                IconButton(onClick = { onClick(item) }) {
                    Image(
                        painter = painterResource(id = item.icon),
                        contentDescription = stringResource(id = item.description),
                        modifier = when (item) {
                            // 🔹 Dark mode icon rotates based on theme
                            ItemsHomeBottomBar.DARKMODE -> Modifier
                                .size(iconSize(35.dp))
                                .rotate(rotationAngle)
                            else -> Modifier.size(iconSize(30.dp))
                        }
                    )
                }
            }
        }
    }
}

/**
 * 🔹 A single icon that shows a small badge (number) in the corner
 * Used for items like Trash and Favorite where counts exist
 */
@Composable
private fun BadgeIcon(
    item: ItemsHomeBottomBar,
    count: Int,
    onClick: (item: ItemsHomeBottomBar) -> Unit,
) {
    // 🔹 Adjust badge position based on icon type
    val yOffset = when (item) {
        ItemsHomeBottomBar.TRASH -> 0.dp
        ItemsHomeBottomBar.FAVORITE -> 5.dp
        else -> 0.dp
    }

    Box(
        modifier = Modifier
            .size(iconSize(30.dp) + 12.dp)
            .clickable { onClick(item) }
            .padding(1.dp),
        contentAlignment = Alignment.Center
    ) {
        // 🔹 Main icon
        Image(
            painter = painterResource(id = item.icon),
            contentDescription = stringResource(id = item.description),
            modifier = Modifier.size(iconSize(30.dp))
        )

        // 🔹 Small red circular badge with count number
        if (count > 0) {
            val badgeText = if (count > 99) "99+" else count.toString()

            Card(
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp, y = yOffset)
            ) {
                Text(
                    text = badgeText,
                    color = MaterialTheme.colorScheme.onError, // Text color inside badge
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.error) // Red background
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                )
            }
        }
    }
}
