package com.irajnajafi1988gmail.notepad.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irajnajafi1988gmail.notepad.domain.model.ItemDarkMode
import com.irajnajafi1988gmail.notepad.domain.usecase.dataStore.darkMode.GetDarkModeStateUseCase
import com.irajnajafi1988gmail.notepad.domain.usecase.dataStore.darkMode.SaveDarkModeStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

// Tag for logging
private const val TAG = "DarkModeViewModel"

// Hilt annotation for dependency injection
@HiltViewModel
class DarkModeViewModel @Inject constructor(
    private val getDarkModeState: GetDarkModeStateUseCase,  // Use case to read dark mode state from DataStore
    private val saveDarkModeState: SaveDarkModeStateUseCase // Use case to save dark mode state to DataStore
) : ViewModel() {

    /**
     * StateFlow to expose the currently selected dark mode state
     * Uses stateIn to convert a Flow to a StateFlow with initial value null
     * SharingStarted.WhileSubscribed(5_000) ensures the flow is active while observed
     */
    val selectedDarkMode: StateFlow<ItemDarkMode?> = getDarkModeState()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    // Initialize by observing changes in dark mode
    init {
        observeDarkModeChanges()
    }

    /**
     * Observe the dark mode state and log changes for debugging purposes
     */
    private fun observeDarkModeChanges() = viewModelScope.launch {
        selectedDarkMode.collectLatest { mode ->
            Log.d(TAG, "🌓 Current Dark Mode: $mode")
        }
    }

    /**
     * Helper function to execute a coroutine action with logging
     * @param actionName: Name of the action for logging
     * @param block: suspend lambda containing the action to execute
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
     * Save the selected dark mode state to DataStore
     */
    fun saveDarkMode(mode: ItemDarkMode) = executeAction("SaveDarkMode") {
        saveDarkModeState(mode)
    }
}
