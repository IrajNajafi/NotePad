package com.irajnajafi1988gmail.notepad.domain.usecase.noteusecase

import com.irajnajafi1988gmail.notepad.data.repository.database.NoteRepository
import com.irajnajafi1988gmail.notepad.domain.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> = repository.getFavoriteNotes()
}
