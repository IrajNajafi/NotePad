package com.irajnajafi1988gmail.notepad.domain.usecase.dataStore.display

import com.irajnajafi1988gmail.notepad.data.repository.datastore.DisplayRepository
import javax.inject.Inject

class SaveDisplayUseCase @Inject constructor(
    private val repository: DisplayRepository
) {
    suspend operator fun invoke(display: Boolean) = repository.saveDisplay(display)

}