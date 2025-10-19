package com.irajnajafi1988gmail.notepad.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irajnajafi1988gmail.notepad.domain.model.Note
import com.irajnajafi1988gmail.notepad.domain.usecase.noteusecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

// Tag for logging
private const val TAG = "NoteViewModel"

// Hilt annotation for dependency injection
@HiltViewModel
class NoteViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,       // Use case to get all notes
    private val getTrashNotesUseCase: GetTrashNotesUseCase,   // Use case to get trashed notes
    private val getFavoriteNotesUseCase: GetFavoriteNotesUseCase, // Use case to get favorite notes
    private val insertNoteUseCase: InsertNoteUseCase,         // Use case to insert a new note
    private val updateNoteUseCase: UpdateNoteUseCase,         // Use case to update an existing note
    private val deleteNoteUseCase: DeleteNoteUseCase          // Use case to delete a note permanently
) : ViewModel() {

    // StateFlow for all notes with UiState (Loading/Success/Error)
    private val _allNotesState = MutableStateFlow<UiState<List<Note>>>(UiState.Loading)
    val allNotesState: StateFlow<UiState<List<Note>>> = _allNotesState

    // StateFlow for trashed notes
    private val _trashedNotesState = MutableStateFlow<UiState<List<Note>>>(UiState.Loading)
    val trashedNotesState: StateFlow<UiState<List<Note>>> = _trashedNotesState

    // StateFlow for favorite notes
    private val _favoriteNotesState = MutableStateFlow<UiState<List<Note>>>(UiState.Loading)
    val favoriteNotesState: StateFlow<UiState<List<Note>>> = _favoriteNotesState


    // Initialize by loading all note states
    init {
        refreshAllStates()
    }

    /**
     * Load notes using the provided use case and update the corresponding StateFlow
     * @param useCase Lambda returning Flow<List<Note>> from a UseCase
     * @param stateFlow StateFlow to update with UiState
     */
    private fun loadNotes(
        useCase: () -> kotlinx.coroutines.flow.Flow<List<Note>>,
        stateFlow: MutableStateFlow<UiState<List<Note>>>
    ) = viewModelScope.launch {
        try {
            stateFlow.value = UiState.Loading  // Show loading state
            delay(400) // Small delay for UI loading effect
            useCase().collectLatest { notes ->
                stateFlow.value = UiState.Success(notes) // Update with success state
            }
        } catch (e: Exception) {
            Log.e(TAG, "loadNotes error", e)
            stateFlow.value = UiState.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    /**
     * Refresh all note-related StateFlows
     */
    private fun refreshAllStates() {
        loadNotes({ getAllNotesUseCase() }, _allNotesState)
        loadNotes({ getFavoriteNotesUseCase() }, _favoriteNotesState)
        loadNotes({ getTrashNotesUseCase() }, _trashedNotesState)
    }

    /**
     * Helper function to perform an action and refresh states afterwards
     */
    private fun performAction(action: suspend () -> Unit) = viewModelScope.launch {
        try {
            action()
            refreshAllStates() // Refresh notes after performing action
        } catch (e: Exception) {
            Log.e(TAG, "performAction error", e)
        }
    }

    /**
     * Add a new note
     * @param note Note object to insert
     * @param onComplete Lambda returning the inserted note ID
     */
    fun addNote(note: Note, onComplete: (Long) -> Unit = {}) = viewModelScope.launch {
        try {
            val id = insertNoteUseCase(note)
            onComplete(id)
            refreshAllStates()
        } catch (e: Exception) {
            Log.e(TAG, "addNote error", e)
        }
    }

    /** Edit an existing note */
    fun editNote(note: Note) = performAction { updateNoteUseCase(note) }

    /** Move a note to trash (soft delete) */
    fun moveNoteToTrash(note: Note) = performAction {
        updateNoteUseCase(note.copy(isTrashed = true))
    }

    /** Restore a note from trash */
    fun restoreNote(note: Note) = performAction {
        updateNoteUseCase(note.copy(isTrashed = false))
    }

    /** Toggle the favorite state of a note */
    fun toggleFavorite(note: Note) = performAction {
        updateNoteUseCase(note.copy(isFavorite = !note.isFavorite))
    }

    /** Delete a note permanently */
    fun deleteNoteForever(note: Note) = performAction { deleteNoteUseCase(note) }

    /** Delete multiple notes permanently */
    fun deleteNotesForever(notes: List<Note>) = viewModelScope.launch {
        notes.forEach { deleteNoteForever(it) }
    }

}
