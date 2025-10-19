package com.irajnajafi1988gmail.notepad.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.irajnajafi1988gmail.notepad.data.local.db.MyDatabase
import com.irajnajafi1988gmail.notepad.data.local.db.entities.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteEntity: NoteEntity):Long

    @Update
    suspend fun updateNote(noteEntity: NoteEntity)

    @Delete
    suspend fun deleteNote(noteEntity: NoteEntity)

    @Query("SELECT *FROM ${MyDatabase.NOTE_TABLE} WHERE id = :noteId LIMIT 1")
    fun getNoteById(noteId: Long): Flow<NoteEntity?>

    @Query("SELECT * fROM ${MyDatabase.NOTE_TABLE} WHERE isTrashed = 0 ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM ${MyDatabase.NOTE_TABLE} WHERE isTrashed = 1 ORDER BY timestamp DESC")
    fun getTrashNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM ${MyDatabase.NOTE_TABLE} WHERE isFavorite = 1 AND isTrashed = 0 ORDER BY timestamp DESC ")
    fun getFavoriteNotes(): Flow<List<NoteEntity>>


}