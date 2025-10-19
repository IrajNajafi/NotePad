package com.irajnajafi1988gmail.notepad.domain.usecase.noteusecase

import com.irajnajafi1988gmail.notepad.data.repository.database.NoteRepository
import com.irajnajafi1988gmail.notepad.domain.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(noteId: Long): Flow<Note?> = repository.getNoteById(noteId)
}
