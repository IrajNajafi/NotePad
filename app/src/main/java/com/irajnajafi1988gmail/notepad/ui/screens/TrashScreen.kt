package com.irajnajafi1988gmail.notepad.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.irajnajafi1988gmail.notepad.R
import com.irajnajafi1988gmail.notepad.domain.model.CheckList
import com.irajnajafi1988gmail.notepad.domain.model.Note
import com.irajnajafi1988gmail.notepad.ui.components.ItemTabBar
import com.irajnajafi1988gmail.notepad.ui.components.common.cardCommon.ItemCardCheckList
import com.irajnajafi1988gmail.notepad.ui.components.common.cardCommon.ItemCardNote
import com.irajnajafi1988gmail.notepad.ui.components.common.dialog.TrashDialog
import com.irajnajafi1988gmail.notepad.ui.viewModel.CheckListViewModel
import com.irajnajafi1988gmail.notepad.ui.viewModel.DateViewModel
import com.irajnajafi1988gmail.notepad.ui.viewModel.NoteViewModel
import com.irajnajafi1988gmail.notepad.ui.viewModel.UiState

@Composable
fun TrashScreen(
    navController: NavController,
    noteViewModel: NoteViewModel = hiltViewModel(),
    checkListViewModel: CheckListViewModel = hiltViewModel(),
    dateViewModel: DateViewModel = hiltViewModel()
) {
    val trashedNotesState = noteViewModel.trashedNotesState.collectAsState(initial = UiState.Loading)
    val trashedCheckListsState = checkListViewModel.trashedNoteListsState.collectAsState(initial = UiState.Loading)

    var selectionMode by remember { mutableStateOf(false) }
    var selectedNotes by remember { mutableStateOf(setOf<Note>()) }
    var selectedCheckLists by remember { mutableStateOf(setOf<CheckList>()) }
    var selectAll by remember { mutableStateOf(false) }

    var itemsToDelete by remember { mutableStateOf<List<Any>>(emptyList()) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ItemTabBar(
                title = stringResource(R.string.trash),
                navigationIcon = R.drawable.back,
                onNavigationClick = { navController.popBackStack() },
                iconSizeDp = 30.dp
            )
        }
    ) { paddingValues ->
        val modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)

        when {
            trashedNotesState.value is UiState.Loading ||
                    trashedCheckListsState.value is UiState.Loading -> {
                Box(modifier = modifier, contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            trashedNotesState.value is UiState.Error ||
                    trashedCheckListsState.value is UiState.Error -> {
                Box(modifier = modifier, contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.error_loading_notes),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            else -> {
                val notes = (trashedNotesState.value as? UiState.Success<List<Note>>)?.data.orEmpty()
                val checkLists = (trashedCheckListsState.value as? UiState.Success<List<CheckList>>)?.data.orEmpty()

                LazyColumn(
                    modifier = modifier.padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    // --- Action Bar ---
                    if (selectionMode) {
                        item {
                            SelectionActionBar(
                                selectedItems = selectedNotes.toList() + selectedCheckLists.toList(),
                                totalItemsCount = notes.size + checkLists.size,
                                selectionAllMode = selectAll,
                                onToggleSelectAll = {
                                    selectAll = it
                                    if (it) {
                                        selectedNotes = notes.toSet()
                                        selectedCheckLists = checkLists.toSet()
                                    } else {
                                        selectedNotes = emptySet()
                                        selectedCheckLists = emptySet()
                                    }
                                },
                                onCancelClick = {
                                    selectionMode = false
                                    selectedNotes = emptySet()
                                    selectedCheckLists = emptySet()
                                    selectAll = false
                                },
                                onDeleteClick = {
                                    val selectedItems = selectedNotes.toList() + selectedCheckLists.toList()
                                    if (selectedItems.isNotEmpty()) {
                                        itemsToDelete = selectedItems
                                        showDeleteDialog = true
                                    }
                                }
                            )
                        }
                    }

                    // --- Trashed Notes ---
                    if (notes.isNotEmpty()) {
                        item { SectionTitle(text = stringResource(R.string.trashed_notes)) }
                        items(notes) { note ->
                            val displayDate = dateViewModel.getFormattedDate(note.timestamp)
                            ItemCardNote(
                                note = note,
                                displayDate = displayDate,
                                isTrashed = true,
                                showCheckbox = selectionMode,
                                isSelected = selectedNotes.contains(note),
                                onCheckChange = { checked ->
                                    selectedNotes = if (checked) selectedNotes + note else selectedNotes - note
                                    selectAll = selectedNotes.size == ((trashedNotesState.value as? UiState.Success)?.data?.size ?: 0)
                                },
                                onClick = {

                                },
                                onLongClick = {
                                    selectionMode = true
                                    selectedNotes = selectedNotes + note
                                },
                                onRestoreClick = { noteViewModel.restoreNote(note) },
                                onDeleteForeverClick = {
                                    itemsToDelete = listOf(note)
                                    showDeleteDialog = true
                                },
                                onFavoriteClick = {},
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // --- Trashed Checklists ---
                    if (checkLists.isNotEmpty()) {
                        item { SectionTitle(text = stringResource(R.string.trashed_checklists)) }
                        items(checkLists) { list ->
                            val displayDate = dateViewModel.getFormattedDate(list.timestamp)
                            ItemCardCheckList(
                                checkList = list,
                                displayDate = displayDate,
                                isTrashed = true,
                                showCheckbox = selectionMode,
                                isSelected = selectedCheckLists.contains(list),
                                onCheckChange = { checked ->
                                    selectedCheckLists = if (checked) selectedCheckLists + list else selectedCheckLists - list
                                    selectAll = selectedCheckLists.size == ((trashedCheckListsState.value as? UiState.Success)?.data?.size ?: 0)
                                },
                                onClick = {

                                },
                                onLongClick = {
                                    selectionMode = true
                                    selectedCheckLists = selectedCheckLists + list
                                },
                                onRestoreClick = { checkListViewModel.restoreCheckList(list) },
                                onDeleteForeverClick = {
                                    itemsToDelete = listOf(list)
                                    showDeleteDialog = true
                                },
                                onFavoriteClick = {},
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // --- Empty state ---
                    if (notes.isEmpty() && checkLists.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillParentMaxSize()
                                    .padding(top = 80.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.the_trash_can_is_empty),
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
    if (showDeleteDialog && itemsToDelete.isNotEmpty()) {
        val deleteMessage = when (itemsToDelete.size) {
            1 -> stringResource(R.string.delete_single_item_confirmation)
            else -> stringResource(R.string.delete_multiple_items_confirmation, itemsToDelete.size)
        }

        TrashDialog(
            text = deleteMessage,
            imageRes = R.drawable.delete_recycle_trash_icon,
            onDismiss = {
                showDeleteDialog = false
                itemsToDelete = emptyList()
            },
            onConfirm = {
                val notesToDelete = itemsToDelete.filterIsInstance<Note>()
                val listsToDelete = itemsToDelete.filterIsInstance<CheckList>()

                if (notesToDelete.isNotEmpty()) noteViewModel.deleteNotesForever(notesToDelete)
                if (listsToDelete.isNotEmpty()) checkListViewModel.deleteCheckListsForever(listsToDelete)

                selectedNotes = emptySet()
                selectedCheckLists = emptySet()
                selectionMode = false
                itemsToDelete = emptyList()
                showDeleteDialog = false
            }
        )
    }


}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 8.dp, start = 8.dp, bottom = 4.dp)
    )
}
