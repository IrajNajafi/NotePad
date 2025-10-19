package com.irajnajafi1988gmail.notepad.domain.repository.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.irajnajafi1988gmail.notepad.data.local.datastore.prefKeys.ThemePrefKeys
import com.irajnajafi1988gmail.notepad.data.local.datastore.provider.ThemeDataStore
import com.irajnajafi1988gmail.notepad.data.repository.datastore.ThemeRepository
import com.irajnajafi1988gmail.notepad.domain.model.ItemDarkMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ThemeRepository {

    /** خواندن وضعیت دارک مود از DataStore */
    override fun getDarkModeState(): Flow<ItemDarkMode> =
        context.ThemeDataStore.data
            .map { preferences ->
                val code = preferences[ThemePrefKeys.DARK_MODE_CODE] ?: ItemDarkMode.SYSTEM.code
                ItemDarkMode.fromCode(code)
            }

    /** ذخیره وضعیت دارک مود در DataStore */
    override suspend fun saveDarkModeState(mode: ItemDarkMode) {
        context.ThemeDataStore.edit { prefs ->
            prefs[ThemePrefKeys.DARK_MODE_CODE] = mode.code
        }
    }
}
