package com.irajnajafi1988gmail.notepad.data.repository.datastore

import com.irajnajafi1988gmail.notepad.domain.model.ItemLanguage
import kotlinx.coroutines.flow.Flow

interface LanguageRepository {
    fun getLanguage(): Flow<ItemLanguage>
    suspend fun saveLanguage(language: ItemLanguage)

}