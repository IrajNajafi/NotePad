package com.irajnajafi1988gmail.notepad.domain.usecase.listusecase

import com.irajnajafi1988gmail.notepad.data.repository.database.ListRepository
import com.irajnajafi1988gmail.notepad.domain.model.CheckList
import javax.inject.Inject

class RestoreListUseCase @Inject constructor(
    private val repository: ListRepository
) {
    suspend operator fun invoke(checkList: CheckList) {
        val restored = checkList.copy(isTrashed = false)
        repository.updateList(restored)
    }
}