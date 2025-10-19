package com.irajnajafi1988gmail.notepad.ui.components.common.cardCommon.cardNoteScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.irajnajafi1988gmail.notepad.domain.model.CheckList
import com.irajnajafi1988gmail.notepad.ui.components.common.cardCommon.CardListScreenCommon
import com.irajnajafi1988gmail.notepad.ui.components.common.cardCommon.ItemCardCheckList
import com.irajnajafi1988gmail.notepad.ui.viewModel.DateViewModel

/**
 * A composable that displays a list of CheckLists using CardListScreenCommon.
 *
 * @param checkLists The list of CheckList items to display
 * @param dateViewModel ViewModel used for formatting timestamps
 * @param onClick Callback when a CheckList card is clicked
 * @param onLongClick Callback when a CheckList card is long-pressed
 * @param onFavoriteClick Callback when the favorite/star icon is clicked
 * @param selectedCheckLists Set of selected CheckLists (for multi-selection mode)
 * @param selectionMode Whether multi-selection mode is active
 * @param onCheckChange Callback when the checkbox state changes for an item
 */
@Composable
fun CardCheckListScreen(
    checkLists: List<CheckList>,
    dateViewModel: DateViewModel,
    onClick: (CheckList) -> Unit,
    onLongClick: (CheckList) -> Unit,
    onFavoriteClick: (CheckList) -> Unit,
    selectedCheckLists: Set<CheckList> = emptySet(),
    selectionMode: Boolean = false,
    onCheckChange: (CheckList, Boolean) -> Unit = { _, _ -> }
) {
    // Use the generic CardListScreenCommon to display a vertical grid/list of items
    CardListScreenCommon(
        items = checkLists,                 // List of items to display
        columns = 1,                        // 1 column for vertical list layout
        dateViewModel = dateViewModel,      // ViewModel for date formatting
        getId = { it.id.toInt() },          // Unique ID for each item
        getTimestamp = { it.timestamp },    // Timestamp for display
        selectedItems = selectedCheckLists, // Currently selected items
        selectionMode = selectionMode,      // Whether selection mode is active
        onCheckChange = onCheckChange       // Checkbox state change handler
    ) { item, displayDate, isSelected ->

        // Render each CheckList item using ItemCardCheckList
        ItemCardCheckList(
            checkList = item,                          // CheckList data
            displayDate = displayDate,                 // Formatted date string
            onClick = { onClick(item) },               // Click handler
            onLongClick = { onLongClick(item) },       // Long press handler
            onFavoriteClick = { onFavoriteClick(item) }, // Favorite toggle
            isSelected = isSelected,                   // Selection state
            showCheckbox = selectionMode,              // Show checkbox in multi-select mode
            onCheckChange = { checked -> onCheckChange(item, checked) }, // Checkbox change
            modifier = Modifier.padding(4.dp)         // Item padding
        )
    }
}
