package com.irajnajafi1988gmail.notepad.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irajnajafi1988gmail.notepad.domain.model.ItemLanguage
import com.irajnajafi1988gmail.notepad.domain.usecase.dataStore.language.GetLanguageStateUseCase
import com.irajnajafi1988gmail.notepad.domain.usecase.dataStore.language.SaveLanguageStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

// Tag for logging
private const val TAG = "LanguageViewModel"

// Hilt annotation for dependency injection
@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val getLanguageStateUseCase: GetLanguageStateUseCase, // Use case to read current language from DataStore
    private val saveLanguageStateUseCase: SaveLanguageStateUseCase // Use case to save selected language to DataStore
) : ViewModel() {

    /**
     * StateFlow representing the currently selected language
     * - stateIn converts the Flow to a StateFlow with initial value null
     * - SharingStarted.WhileSubscribed(5_000) keeps the flow active while observed
     */
    val selectedLanguage: StateFlow<ItemLanguage?> = getLanguageStateUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    // Initialize by observing language changes for logging/debugging
    init {
        observeLanguageChanges()
    }

    /**
     * Collect the selectedLanguage flow and log changes
     * Useful for debugging and observing language state changes
     */
    private fun observeLanguageChanges() {
        viewModelScope.launch {
            selectedLanguage.collectLatest { language ->
                Log.d(TAG, "🗣️ Current Language: $language")
            }
        }
    }

    /**
     * Helper function to execute a coroutine action with logging
     * @param actionName Name of the action for logging
     * @param block suspend lambda containing the action to execute
     */
    private inline fun executeAction(
        actionName: String,
        crossinline block: suspend () -> Unit
    ) = viewModelScope.launch {
        Log.d(TAG, "$actionName started") // Log start
        runCatching { block() }            // Execute the action safely
            .onSuccess { Log.d(TAG, "$actionName completed successfully") } // Log success
            .onFailure { e -> Log.e(TAG, "Error during $actionName", e) }  // Log failure
    }

    /**
     * Save the selected language to DataStore
     */
    fun saveLanguage(language: ItemLanguage) = executeAction("SaveLanguage") {
        saveLanguageStateUseCase(language)
    }
}
