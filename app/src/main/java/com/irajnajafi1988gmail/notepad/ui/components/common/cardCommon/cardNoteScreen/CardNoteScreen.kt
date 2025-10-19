package com.irajnajafi1988gmail.notepad.ui.components.common.cardCommon.cardNoteScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.irajnajafi1988gmail.notepad.domain.model.Note
import com.irajnajafi1988gmail.notepad.ui.components.common.cardCommon.CardListScreenCommon
import com.irajnajafi1988gmail.notepad.ui.components.common.cardCommon.ItemCardNote
import com.irajnajafi1988gmail.notepad.ui.viewModel.DateViewModel

/**
 * A composable that displays a list of Notes using CardListScreenCommon.
 *
 * @param notes The list of Note items to display
 * @param dateViewModel ViewModel used for formatting timestamps
 * @param onClick Callback when a Note card is clicked
 * @param onLongClick Callback when a Note card is long-pressed
 * @param onFavoriteClick Callback when the favorite/star icon is clicked
 * @param selectedNotes Set of selected Notes (for multi-selection mode)
 * @param selectionMode Whether multi-selection mode is active
 * @param onCheckChange Callback when the checkbox state changes for an item
 */
@Composable
fun CardNoteScreen(
    notes: List<Note>,
    dateViewModel: DateViewModel,
    onClick: (Note) -> Unit,
    onLongClick: (Note) -> Unit,
    onFavoriteClick: (Note) -> Unit,
    selectedNotes: Set<Note> = emptySet(),
    selectionMode: Boolean = false,
    onCheckChange: (Note, Boolean) -> Unit = { _, _ -> }
) {
    // Use the generic CardListScreenCommon to display a grid of notes
    CardListScreenCommon(
        items = notes,                        // List of Note items
        columns = 2,                          // 2 columns for grid layout
        dateViewModel = dateViewModel,        // ViewModel for formatting date
        getId = { it.id.toInt() },            // Unique ID for each note
        getTimestamp = { it.timestamp },      // Timestamp for each note
        selectedItems = selectedNotes,        // Set of currently selected notes
        selectionMode = selectionMode,        // Multi-selection mode flag
        onCheckChange = onCheckChange         // Checkbox state change callback
    ) { note, displayDate, isSelected ->

        // Render each Note using ItemCardNote composable
        ItemCardNote(
            note = note,                        // Note data
            displayDate = displayDate,          // Formatted timestamp
            onClick = { onClick(note) },        // Click handler
            onLongClick = { onLongClick(note) },// Long press handler
            onFavoriteClick = { onFavoriteClick(note) }, // Favorite toggle
            isSelected = isSelected,            // Whether note is selected
            showCheckbox = selectionMode,       // Show checkbox if in selection mode
            onCheckChange = { checked -> onCheckChange(note, checked) }, // Checkbox callback
            modifier = Modifier.padding(4.dp)   // Card padding
        )
    }
}
