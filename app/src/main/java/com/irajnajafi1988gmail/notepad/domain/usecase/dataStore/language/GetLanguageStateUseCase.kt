package com.irajnajafi1988gmail.notepad.domain.usecase.dataStore.language

import com.irajnajafi1988gmail.notepad.data.repository.datastore.LanguageRepository
import com.irajnajafi1988gmail.notepad.domain.model.ItemLanguage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLanguageStateUseCase @Inject constructor(
    private val repository: LanguageRepository
) {

    operator fun invoke() : Flow<ItemLanguage> = repository.getLanguage()
}