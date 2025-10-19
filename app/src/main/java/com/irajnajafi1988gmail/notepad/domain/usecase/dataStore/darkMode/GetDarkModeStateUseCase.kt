package com.irajnajafi1988gmail.notepad.domain.usecase.dataStore.darkMode

import com.irajnajafi1988gmail.notepad.data.repository.datastore.ThemeRepository
import com.irajnajafi1988gmail.notepad.domain.model.ItemDarkMode
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDarkModeStateUseCase @Inject constructor(
    private val repository: ThemeRepository
) {
    operator fun invoke(): Flow<ItemDarkMode> = repository.getDarkModeState()



}