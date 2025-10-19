package com.irajnajafi1988gmail.notepad.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.irajnajafi1988gmail.notepad.data.local.db.MyDatabase
import com.irajnajafi1988gmail.notepad.domain.utils.AppDate

@Entity(tableName = MyDatabase.NOTE_TABLE)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val isTrashed: Boolean = false
)
