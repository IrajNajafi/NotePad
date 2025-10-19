package com.irajnajafi1988gmail.notepad.domain.model

import com.irajnajafi1988gmail.notepad.data.model.CheckItem

data class CheckList(
    val id: Long = 0L,
    val title: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val isTrashed: Boolean = false,
    val items: List<CheckItem> = emptyList(),

)
   
