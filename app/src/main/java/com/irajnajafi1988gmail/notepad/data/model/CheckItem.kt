package com.irajnajafi1988gmail.notepad.data.model

import androidx.compose.ui.text.input.TextFieldValue

data class CheckItem(
    val text: String = "",
    val isCheckItem: Boolean = false,
    val isTrashed: Boolean = false
)
