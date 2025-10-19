package com.irajnajafi1988gmail.notepad.data.repository.database

import com.irajnajafi1988gmail.notepad.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun insertNote(note: Note): Long
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
    fun getNoteById(noteId: Long): Flow<Note?>
    fun getAllNotes(): Flow<List<Note>>
    fun getTrashNotes(): Flow<List<Note>>
    fun getFavoriteNotes(): Flow<List<Note>>



}