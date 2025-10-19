package com.irajnajafi1988gmail.notepad.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.irajnajafi1988gmail.notepad.data.local.db.MyDatabase
import com.irajnajafi1988gmail.notepad.data.model.CheckItem
import java.util.Date

@Entity(tableName = MyDatabase.LIST_TABLE)
data class ListEntity (
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0L,
    val title: String = "",
    val content:String = "",
    val timestamp: Long,
    val isFavorite:Boolean = false,
    val items: List<CheckItem> = emptyList(),
    val isTrashed: Boolean = false



)
