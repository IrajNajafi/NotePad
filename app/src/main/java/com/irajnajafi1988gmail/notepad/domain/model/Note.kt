package com.irajnajafi1988gmail.notepad.domain.model

data class Note(
    val id: Long = 0L,
    val title: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val isTrashed: Boolean = false,
    val isCheckBox: Boolean = false
)
