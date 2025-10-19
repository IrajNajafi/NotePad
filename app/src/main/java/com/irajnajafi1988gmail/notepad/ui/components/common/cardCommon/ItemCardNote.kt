package com.irajnajafi1988gmail.notepad.ui.components.common.cardCommon

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.irajnajafi1988gmail.notepad.R
import com.irajnajafi1988gmail.notepad.domain.model.Note

@Composable
fun ItemCardNote(
    note: Note,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onRestoreClick: (() -> Unit)? = null,
    onDeleteForeverClick: (() -> Unit)? = null,
    isTrashed: Boolean = false,
    isSelected: Boolean = false,
    showCheckbox: Boolean = false,
    onCheckChange: (Boolean) -> Unit = {},
    displayDate: String,
   modifier: Modifier
) {
    ItemCardCommon(
        title = note.title,
        content = note.content,
        isFavorite = note.isFavorite,
        isTrashed = isTrashed,
        isSelected = isSelected,
        showCheckbox = showCheckbox,
        displayDate = displayDate,
        onClick = onClick,
        onLongClick = onLongClick,
        onFavoriteClick = onFavoriteClick,
        onRestoreClick = onRestoreClick,
        onCheckChange = onCheckChange,
        emptyTitleRes = R.string.untitled_note,
        modifier = modifier
    )
}
