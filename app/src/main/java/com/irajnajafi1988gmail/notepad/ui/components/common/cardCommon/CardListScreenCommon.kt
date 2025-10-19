package com.irajnajafi1988gmail.notepad.ui.components.common.cardCommon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.irajnajafi1988gmail.notepad.ui.viewModel.DateViewModel

/**
 * A reusable composable for displaying a list of items in a grid.
 * Supports selection mode and dynamically formats item timestamps using DateViewModel.
 *
 * @param T Type of the items in the list
 * @param items List of items to display
 * @param columns Number of columns in the grid (default 2)
 * @param dateViewModel Used to format timestamps for display
 * @param getId Lambda to get a unique ID for each item (used for LazyVerticalGrid key)
 * @param getTimestamp Lambda to get the timestamp of each item
 * @param selectedItems Set of currently selected items (for selection mode)
 * @param selectionMode If true, selection checkboxes or highlights can be shown
 * @param onCheckChange Callback when an item is selected/deselected
 * @param itemContent Composable lambda that defines how each item should be displayed
 */
@Composable
fun <T> CardListScreenCommon(
    items: List<T>,
    columns: Int = 2,
    dateViewModel: DateViewModel,
    getId: (T) -> Int,
    getTimestamp: (T) -> Long,
    selectedItems: Set<T> = emptySet(),
    selectionMode: Boolean = false,
    onCheckChange: (T, Boolean) -> Unit = { _, _ -> },
    itemContent: @Composable (item: T, displayDate: String, isSelected: Boolean) -> Unit
) {
    // LazyVerticalGrid displays items in a scrollable grid
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns), // fixed number of columns
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp), // padding around the grid
        verticalArrangement = Arrangement.spacedBy(8.dp), // vertical spacing between items
        horizontalArrangement = Arrangement.spacedBy(8.dp) // horizontal spacing between items
    ) {
        items(
            items = items,
            key = { getId(it) } // unique key for each item for efficient recomposition
        ) { item ->
            // Format the timestamp using DateViewModel
            val displayDate = dateViewModel.getFormattedDate(getTimestamp(item))
            // Check if this item is currently selected
            val isSelected = selectedItems.contains(item)

            // Render the item using the provided composable lambda
            itemContent(item, displayDate, isSelected)
        }
    }
}
