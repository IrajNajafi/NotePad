package com.irajnajafi1988gmail.notepad.domain.usecase.dataStore.language

import com.irajnajafi1988gmail.notepad.data.repository.datastore.LanguageRepository
import com.irajnajafi1988gmail.notepad.domain.model.ItemLanguage
import javax.inject.Inject

class SaveLanguageStateUseCase @Inject constructor(
    private val repository: LanguageRepository
) {
    suspend operator fun invoke(language: ItemLanguage) = repository.saveLanguage(language)
}