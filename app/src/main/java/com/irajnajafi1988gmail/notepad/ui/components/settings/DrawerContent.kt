package com.irajnajafi1988gmail.notepad.ui.components.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.irajnajafi1988gmail.notepad.MainActivity
import com.irajnajafi1988gmail.notepad.R
import com.irajnajafi1988gmail.notepad.domain.model.ItemDarkMode
import com.irajnajafi1988gmail.notepad.domain.model.ItemLanguage
import com.irajnajafi1988gmail.notepad.ui.viewModel.DarkModeViewModel
import com.irajnajafi1988gmail.notepad.ui.viewModel.LanguageViewModel
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(
    onItemClick: (SettingItem) -> Unit, // Callback when a main setting item is clicked
    darkModeViewModel: DarkModeViewModel = hiltViewModel(), // ViewModel for managing dark mode state
    languageViewModel: LanguageViewModel = hiltViewModel() // ViewModel for managing language state
) {
    // Remember the expanded/collapsed state of each SettingItem
    val expandedStates = remember { mutableStateMapOf<SettingItem, Boolean>() }

    // Collect current selected dark mode from ViewModel
    val darkMode by darkModeViewModel.selectedDarkMode.collectAsState()

    // Collect current selected language from ViewModel
    val selectedLanguage by languageViewModel.selectedLanguage.collectAsState()

    // Coroutine scope for asynchronous operations
    val coroutineScope = rememberCoroutineScope()

    // Get current Activity context to recreate when language changes
    val context = LocalContext.current
    val activity = context as? MainActivity

    // Main container column for the drawer content
    Column(
        modifier = Modifier
            .fillMaxHeight() // Fill the vertical space
            .padding(top = 40.dp) // Add top padding from status bar / top
    ) {
        // Loop through all main setting items
        SettingItem.items.forEach { item ->
            // Row for the main setting item with icon and title
            SettingRow(
                iconRes = item.iconRes, // Icon resource for the setting
                titleRes = item.titleRes, // Title resource ID
                expanded = expandedStates[item] == true, // Whether the sub-menu is expanded
                onClick = {
                    // Toggle expanded/collapsed state on click
                    expandedStates[item] = !(expandedStates[item] ?: false)
                }
            )

            // Sub-items animation: expand or shrink vertically
            AnimatedVisibility(
                visible = expandedStates[item] == true,
                enter = expandVertically(), // Smooth expand
                exit = shrinkVertically()   // Smooth collapse
            ) {
                // Display the corresponding sub-items based on the main setting
                when (item) {
                    SettingItem.DARKMODE -> {
                        // Column containing dark mode options
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 56.dp, top = 4.dp, bottom = 8.dp) // Indent for sub-items
                        ) {
                            listOf(
                                ItemDarkMode.DARK,
                                ItemDarkMode.LIGHT,
                                ItemDarkMode.SYSTEM
                            ).forEach { mode ->
                                // Each sub-row represents one dark mode option
                                SettingSubRow(
                                    title = stringResource(id = mode.text), // Text label
                                    imageRes = mode.icon, // Icon for the mode
                                    imageCheckItem = R.drawable.tick, // Checkmark when selected
                                    isChecked = darkMode == mode, // Check if this mode is active
                                    onClick = { darkModeViewModel.saveDarkMode(mode) }, // Save selection
                                    iconSizeDp = 18.dp // Icon size
                                )
                            }
                        }
                    }

                    SettingItem.LANGUAGE -> {
                        // Column containing language options
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 56.dp, top = 4.dp, bottom = 8.dp)
                        ) {
                            ItemLanguage.entries.forEach { language ->
                                SettingSubRow(
                                    title = stringResource(id = language.textRes), // Language name
                                    imageRes = 0, // No icon
                                    trailingText = language.flag, // Show flag emoji
                                    imageCheckItem = R.drawable.tick, // Checkmark if selected
                                    isChecked = selectedLanguage == language, // Check if this language is active
                                    onClick = {
                                        coroutineScope.launch {
                                            languageViewModel.saveLanguage(language) // Save new language
                                            kotlinx.coroutines.delay(1000) // Wait to ensure saving is complete
                                            activity?.recreate() // Recreate activity to apply language
                                        }
                                    },
                                    iconSizeDp = 18.dp,
                                )
                            }
                        }
                    }

                    SettingItem.INFO -> {
                        // Column for app info like contact and version
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 56.dp, top = 4.dp, bottom = 8.dp)
                        ) {
                            SettingSubRow(
                                title = "irajnajafi1988@gmail.com", // Contact email
                                imageRes = R.drawable.gmail, // Gmail icon
                                imageCheckItem = null, // No checkmark
                                onClick = {}, // No action
                                iconSizeDp = 18.dp,
                                textSizeSp = 10.sp // Smaller text
                            )

                            SettingSubRow(
                                title = stringResource(id = R.string.program_version_1_0_0), // App version
                                imageRes = R.drawable.tag, // Icon for version
                                imageCheckItem = null,
                                onClick = {},
                                iconSizeDp = 15.dp,
                                textSizeSp = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
