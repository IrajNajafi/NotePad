package com.irajnajafi1988gmail.notepad.data.repository.datastore

import com.irajnajafi1988gmail.notepad.domain.model.ItemDarkMode
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun getDarkModeState(): Flow<ItemDarkMode>
    suspend fun saveDarkModeState(mode: ItemDarkMode)
}