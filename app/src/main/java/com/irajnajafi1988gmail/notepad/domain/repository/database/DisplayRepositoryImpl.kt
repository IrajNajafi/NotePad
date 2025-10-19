package com.irajnajafi1988gmail.notepad.domain.repository.database

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.irajnajafi1988gmail.notepad.data.local.datastore.prefKeys.DisplayPrefKeys
import com.irajnajafi1988gmail.notepad.data.local.datastore.provider.displayDataStore
import com.irajnajafi1988gmail.notepad.data.repository.datastore.DisplayRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DisplayRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : DisplayRepository {
    override val getDisplay: Flow<Boolean> = context.displayDataStore.data
        .map { preferences ->
            preferences[DisplayPrefKeys.DISPLAY_KEY] ?: false


        }

    override suspend fun saveDisplay(display: Boolean) {
        context.displayDataStore.edit { preferences ->
            preferences[DisplayPrefKeys.DISPLAY_KEY] = display
        }

    }

    override suspend fun toggleDisplay() {

        val currentDisplay = getDisplay.first()
        saveDisplay(!currentDisplay)


    }
}