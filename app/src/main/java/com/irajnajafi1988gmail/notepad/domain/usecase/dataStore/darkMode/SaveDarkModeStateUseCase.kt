package com.irajnajafi1988gmail.notepad.domain.usecase.dataStore.darkMode

import com.irajnajafi1988gmail.notepad.data.repository.datastore.ThemeRepository
import com.irajnajafi1988gmail.notepad.domain.model.ItemDarkMode
import javax.inject.Inject



    class SaveDarkModeStateUseCase @Inject constructor(
        private val repository: ThemeRepository
    ) {
        suspend operator fun invoke(mode: ItemDarkMode) = repository.saveDarkModeState(mode)
    }