package com.irajnajafi1988gmail.notepad.domain.usecase.dataStore.display

import com.irajnajafi1988gmail.notepad.data.repository.datastore.DisplayRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDisplayUseCase @Inject constructor(
    private val repository: DisplayRepository
) {
    operator fun invoke(): Flow<Boolean> = repository.getDisplay

}