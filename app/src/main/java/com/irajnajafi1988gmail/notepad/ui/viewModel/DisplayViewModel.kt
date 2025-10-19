package com.irajnajafi1988gmail.notepad.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irajnajafi1988gmail.notepad.domain.usecase.dataStore.display.GetDisplayUseCase
import com.irajnajafi1988gmail.notepad.domain.usecase.dataStore.display.SaveDisplayUseCase
import com.irajnajafi1988gmail.notepad.domain.model.ToggleNotePad // Import ToggleNotePad enum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

// Tag for logging
private const val TAG = "DisplayViewModel"

// Hilt annotation for dependency injection
@HiltViewModel
class DisplayViewModel @Inject constructor(
    private val getDisplayUseCase: GetDisplayUseCase, // Use case to get current display type (Note or CheckList)
    private val saveDisplayUseCase: SaveDisplayUseCase // Use case to save selected display type
) : ViewModel() {

    /**
     * StateFlow representing the currently selected display mode (NOTE or CHECKLIST)
     * - distinctUntilChanged ensures updates only when the value actually changes
     * - map converts a Boolean (from DataStore) into the ToggleNotePad enum
     * - stateIn converts the Flow to StateFlow with initial value NOTE
     */
    val selectedDisplay: StateFlow<ToggleNotePad> = getDisplayUseCase()
        .distinctUntilChanged()
        .map { isCheckList ->
            if (isCheckList) ToggleNotePad.CHECKLIST else ToggleNotePad.NOTE
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ToggleNotePad.NOTE
        )

    /**
     * Helper function to execute a coroutine action with logging
     * @param actionName: name of the action for logging purposes
     * @param block: suspend lambda containing the action to execute
     */
    private inline fun executeAction(
        actionName: String,
        crossinline block: suspend () -> Unit
    ) = viewModelScope.launch {
        Log.d(TAG, "$actionName started") // Log action start
        runCatching { block() }            // Safely run the action
            .onSuccess { Log.d(TAG, "$actionName completed successfully") } // Log success
            .onFailure { e -> Log.e(TAG, "Error during $actionName", e) }  // Log failure
    }

    /**
     * Save the selected display mode to DataStore
     */
    fun saveDisplay(display: ToggleNotePad) = executeAction("SaveDisplay") {
        val isCheckList = display == ToggleNotePad.CHECKLIST
        saveDisplayUseCase(isCheckList)
    }

    /**
     * Toggle the display mode between NOTE and CHECKLIST
     * Saves the new state after toggling
     */
    fun toggleDisplay() = executeAction("ToggleDisplay") {
        val current = selectedDisplay.value
        val newDisplay = when (current) {
            ToggleNotePad.NOTE -> ToggleNotePad.CHECKLIST
            ToggleNotePad.CHECKLIST -> ToggleNotePad.NOTE
        }

        saveDisplay(newDisplay) // Save the toggled display mode
    }
}
