package com.irajnajafi1988gmail.notepad.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.irajnajafi1988gmail.notepad.data.local.db.converters.Converters
import com.irajnajafi1988gmail.notepad.data.local.db.dao.ListDao
import com.irajnajafi1988gmail.notepad.data.local.db.dao.NoteDao
import com.irajnajafi1988gmail.notepad.data.local.db.entities.ListEntity
import com.irajnajafi1988gmail.notepad.data.local.db.entities.NoteEntity

@Database(
    entities = [NoteEntity::class ,ListEntity::class],
    version =MyDatabase.VERSION,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MyDatabase : RoomDatabase() {
    companion object{
        const val VERSION = 1
        const val DBNAME = "notePad_db"
        const val NOTE_TABLE = "notes"
        const val LIST_TABLE ="listS"
    }
    abstract fun  noteDao():NoteDao
    abstract fun  listDao():ListDao
}