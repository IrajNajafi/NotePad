package com.irajnajafi1988gmail.notepad.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.irajnajafi1988gmail.notepad.R
import com.irajnajafi1988gmail.notepad.domain.model.CheckList
import com.irajnajafi1988gmail.notepad.domain.model.Note
import com.irajnajafi1988gmail.notepad.navigation.NaveScreen
import com.irajnajafi1988gmail.notepad.ui.components.ItemTabBar
import com.irajnajafi1988gmail.notepad.ui.components.common.cardCommon.ItemCardCheckList
import com.irajnajafi1988gmail.notepad.ui.components.common.cardCommon.ItemCardNote
import com.irajnajafi1988gmail.notepad.ui.viewModel.CheckListViewModel
import com.irajnajafi1988gmail.notepad.ui.viewModel.DateViewModel
import com.irajnajafi1988gmail.notepad.ui.viewModel.NoteViewModel
import com.irajnajafi1988gmail.notepad.ui.viewModel.UiState

/**
 * ✅ FavoriteScreen
 * Shows all favorite notes and checklists together.
 * Handles loading, empty, and error states properly.
 */
@Composable
fun FavoriteScreen(
    navController: NavController,
    noteViewModel: NoteViewModel = hiltViewModel(),
    checkListViewModel: CheckListViewModel = hiltViewModel(),
    dateViewModel: DateViewModel = hiltViewModel()
) {
    // Observe the current state of favorite notes and checklists
    val favoriteNotesState = noteViewModel.favoriteNotesState.collectAsState(initial = UiState.Loading)
    val favoriteCheckListsState = checkListViewModel.favoriteNoteListsState.collectAsState(initial = UiState.Loading)

    Scaffold(
        // 🧭 Top Bar with back button and title
        topBar = {
            ItemTabBar(
                title = stringResource(R.string.favorites),
                navigationIcon = R.drawable.back,
                onNavigationClick = { navController.popBackStack() },
                iconSizeDp = 30.dp
            )
        },
    ) { paddingValues ->

        val modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)

        when {
            // ⏳ Loading state
            favoriteNotesState.value is UiState.Loading ||
                    favoriteCheckListsState.value is UiState.Loading -> {
                Box(modifier = modifier, contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            // ❌ Error state
            favoriteNotesState.value is UiState.Error ||
                    favoriteCheckListsState.value is UiState.Error -> {
                Box(modifier = modifier, contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.error_loading_notes),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            // ✅ Success state
            else -> {
                LazyColumn(
                    modifier = modifier.padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    // --- Favorite Notes Section ---
                    val notes = (favoriteNotesState.value as? UiState.Success<List<Note>>)?.data.orEmpty()
                    if (notes.isNotEmpty()) {
                        item { SectionTitleFavorite(text = stringResource(R.string.favorite_notes)) }

                        // Display notes in 2-column rows
                        items(notes.chunked(2)) { notePair ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                notePair.forEach { note ->
                                    val displayDate = dateViewModel.getFormattedDate(note.timestamp)
                                    ItemCardNote(
                                        modifier = Modifier.weight(1f),
                                        note = note,
                                        onClick = {
                                            navController.navigate("${NaveScreen.AddNoteScreen.route}/${note.id}")
                                        },
                                        onLongClick = {},
                                        onFavoriteClick = { noteViewModel.toggleFavorite(note) },
                                        displayDate = displayDate,

                                    )
                                }
                                // Fill remaining space if row has only 1 item
                                repeat(2 - notePair.size) { Spacer(modifier = Modifier.weight(1f)) }
                            }
                        }
                    }

                    // --- Favorite Checklists Section ---
                    val checkLists =
                        (favoriteCheckListsState.value as? UiState.Success<List<CheckList>>)?.data.orEmpty()

                    if (checkLists.isNotEmpty()) {
                        item { SectionTitleFavorite(text = stringResource(R.string.favorites_checklists)) }

                        items(checkLists) { checkList ->
                            val displayDate = dateViewModel.getFormattedDate(checkList.timestamp)
                            ItemCardCheckList(
                                checkList = checkList,
                                onClick = {
                                    navController.navigate("${NaveScreen.AddCheckListScreen.route}/${checkList.id}")
                                },
                                onLongClick = {},
                                onFavoriteClick = { checkListViewModel.toggleFavorite(checkList) },
                                displayDate = displayDate,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)

                            )
                        }
                    }

                    // --- Empty State ---
                    if (notes.isEmpty() && checkLists.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillParentMaxSize()
                                    .padding(top = 80.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.favorites_are_empty),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * ✅ SectionTitleFavorite
 * Displays section titles like "Favorite Notes" or "Favorite Checklists".
 */
@Composable
private fun SectionTitleFavorite(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 8.dp, start = 8.dp, bottom = 4.dp)
    )
}
