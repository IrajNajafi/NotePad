package com.irajnajafi1988gmail.notepad.ui.components.common.bottombar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.irajnajafi1988gmail.notepad.R
import com.irajnajafi1988gmail.notepad.ui.theme.helpers.iconSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Bottom bar for Note App that allows zoom in/out or save actions.
 *
 * @param isTitleActive Determines if title-related actions are shown
 * @param selectedItem Currently selected bottom bar item
 * @param onItemClick Callback when a bottom bar item is clicked or pressed
 * @param modifier Modifier for customization
 */
@Composable
fun NoteAppBottomBar(
    isTitleActive: Boolean,
    selectedItem: ItemAppBottomBar?,
    onItemClick: (item: ItemAppBottomBar) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Determine which items to show based on whether the title is active
    val items = if (isTitleActive) {
        listOf(
            ItemAppBottomBar.SMALL_TITLE,
            ItemAppBottomBar.BIG_TITLE,
            ItemAppBottomBar.SAVE
        )
    } else {
        listOf(
            ItemAppBottomBar.SMALL_BODY,
            ItemAppBottomBar.BIG_BODY,
            ItemAppBottomBar.SAVE
        )
    }

    // Outer Card container for bottom bar
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 25.dp)
            .imePadding(), // Adjust for keyboard
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val scope = rememberCoroutineScope() // Coroutine scope for repeated presses

            items.forEach { item ->
                // Determine label for each item
                val label = when (item) {
                    ItemAppBottomBar.SMALL_TITLE,
                    ItemAppBottomBar.SMALL_BODY -> stringResource(R.string.zoom_out)
                    ItemAppBottomBar.BIG_TITLE,
                    ItemAppBottomBar.BIG_BODY -> stringResource(R.string.zoom_in)
                    ItemAppBottomBar.SAVE -> stringResource(R.string.save)
                }

                Column(
                    modifier = Modifier
                        .width(64.dp)
                        .padding(vertical = 2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Clickable Box for each button
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                color = if (selectedItem == item) Color(0xFFEAEAEA)
                                else Color.Transparent,
                            )
                            .pointerInput(item) {
                                detectTapGestures(
                                    onPress = {
                                        // Launch coroutine for repeated actions on long press
                                        val job = scope.launch {
                                            onItemClick(item) // initial click
                                            delay(300L)
                                            while (true) {
                                                onItemClick(item) // repeat click every 100ms
                                                delay(100L)
                                            }
                                        }
                                        tryAwaitRelease() // wait until release
                                        job.cancel()      // cancel coroutine when released
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        // Icon for the item
                        Image(
                            painter = painterResource(id = item.iconRes),
                            contentDescription = label,
                            modifier = Modifier.size(iconSize(22.dp))
                        )
                    }

                    // Label text below the icon
                    Text(
                        text = label,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}
