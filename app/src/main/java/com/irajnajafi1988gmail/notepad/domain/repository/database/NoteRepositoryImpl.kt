package com.irajnajafi1988gmail.notepad.domain.repository.database

import com.irajnajafi1988gmail.notepad.data.local.db.dao.NoteDao
import com.irajnajafi1988gmail.notepad.data.local.mapper.toDomain
import com.irajnajafi1988gmail.notepad.data.local.mapper.toEntity
import com.irajnajafi1988gmail.notepad.data.repository.database.NoteRepository
import com.irajnajafi1988gmail.notepad.domain.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,

    ) : NoteRepository {

    override suspend fun insertNote(note: Note): Long {
        return noteDao.insertNote(note.toEntity())
    }

    override suspend fun updateNote(note: Note) {
        return noteDao.updateNote(note.toEntity())

    }

    override suspend fun deleteNote(note: Note) {
        return noteDao.deleteNote(note.toEntity())
    }

    override fun getNoteById(noteId: Long): Flow<Note?> {
        return noteDao.getNoteById(noteId).map { it?.toDomain() }
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { list -> list.map { it.toDomain() } }
    }

    override fun getTrashNotes(): Flow<List<Note>> = noteDao.getTrashNotes().map { list -> list.map { it.toDomain() } }


    override fun getFavoriteNotes(): Flow<List<Note>> {
        return noteDao.getFavoriteNotes().map { list -> list.map { it.toDomain() } }
    }




}