package com.irajnajafi1988gmail.notepad.domain.usecase.listusecase

import com.irajnajafi1988gmail.notepad.data.repository.database.ListRepository
import com.irajnajafi1988gmail.notepad.data.repository.database.NoteRepository
import com.irajnajafi1988gmail.notepad.domain.model.Note
import com.irajnajafi1988gmail.notepad.domain.model.CheckList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetListByIdUseCase @Inject constructor(
    private val repository: ListRepository
) {
    operator fun invoke(listId: Long): Flow<CheckList?> = repository.getListBydId(listId)
}
