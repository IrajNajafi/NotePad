package com.irajnajafi1988gmail.notepad.ui.components.settings

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun SettingsDrawer(
    drawerState: DrawerState,
    onItemClick: (SettingItem) -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope() // Coroutine scope for opening/closing the drawer
    val layoutDirection = LocalLayoutDirection.current
    val isRtl = layoutDirection == LayoutDirection.Rtl // Check if layout is Right-to-Left

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(0.7f) // Drawer width: 70% of screen
                    .fillMaxHeight(),     // Drawer height: full screen
                drawerShape = RoundedCornerShape(
                    // Rounded corners depending on LTR/RTL layout
                    topEnd = if (isRtl) 20.dp else 0.dp,
                    bottomEnd = if (isRtl) 20.dp else 0.dp,
                    topStart = if (isRtl) 0.dp else 20.dp,
                    bottomStart = if (isRtl) 0.dp else 20.dp
                )
            ) {
                // Drawer content: list of settings
                DrawerContent(
                    onItemClick = { item ->
                        scope.launch { drawerState.close() } // Close drawer with animation
                        onItemClick(item) // Trigger the selected item's action
                    }
                )
            }
        },
        drawerState = drawerState,
        gesturesEnabled = true, // Enable swipe gestures to open/close drawer
        modifier = Modifier,
        scrimColor = Color.Black.copy(alpha = 0.3f) // Semi-transparent overlay behind drawer
    ) {
        content() // Main screen content shown under the drawer
    }
}
