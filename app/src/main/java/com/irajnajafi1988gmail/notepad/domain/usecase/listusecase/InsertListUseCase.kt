package com.irajnajafi1988gmail.notepad.domain.usecase.listusecase

import com.irajnajafi1988gmail.notepad.data.repository.database.ListRepository
import com.irajnajafi1988gmail.notepad.domain.model.CheckList
import javax.inject.Inject

class InsertListUseCase @Inject constructor(
    private val repository: ListRepository
) {
    suspend operator fun invoke(checkList: CheckList): Long = repository.insertList(checkList)
}
