package com.irajnajafi1988gmail.notepad.domain.repository.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.irajnajafi1988gmail.notepad.data.local.datastore.prefKeys.LanguagePrefKeys
import com.irajnajafi1988gmail.notepad.data.local.datastore.provider.LanguageDataStore
import com.irajnajafi1988gmail.notepad.data.repository.datastore.LanguageRepository
import com.irajnajafi1988gmail.notepad.domain.model.ItemLanguage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LanguageRepository {
    override fun getLanguage(): Flow<ItemLanguage> =
        context.LanguageDataStore.data
            .map { preferences ->
                val code = preferences[LanguagePrefKeys.LANGUAGE_CODE] ?: ItemLanguage.ENGLISH.code
                ItemLanguage.fromCode(code)


            }

    override suspend fun saveLanguage(language: ItemLanguage) {
        context.LanguageDataStore.edit { prefs ->
            prefs[LanguagePrefKeys.LANGUAGE_CODE] = language.code
        }

    }
}