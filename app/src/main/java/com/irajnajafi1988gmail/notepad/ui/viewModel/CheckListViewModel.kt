package com.irajnajafi1988gmail.notepad.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irajnajafi1988gmail.notepad.domain.model.CheckList
import com.irajnajafi1988gmail.notepad.domain.usecase.listusecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

// Tag for logging
private const val TAG = "CheckListViewModel"

// Hilt annotation for dependency injection
@HiltViewModel
class CheckListViewModel @Inject constructor(
    private val getAllListsUseCase: GetAllListsUseCase,       // Use case to get all checklists
    private val getTrashListsUseCase: GetTrashListsUseCase,   // Use case to get trashed checklists
    private val getFavoriteListsUseCase: GetFavoriteListsUseCase, // Use case to get favorite checklists
    private val insertListUseCase: InsertListUseCase,         // Use case to insert a new checklist
    private val updateListUseCase: UpdateListUseCase,         // Use case to update an existing checklist
    private val deleteListUseCase: DeleteListUseCase,         // Use case to delete a checklist permanently
    private val restoreListUseCase: RestoreListUseCase        // Use case to restore a trashed checklist
) : ViewModel() {

    // StateFlow to hold all checklists with UiState wrapper
    private val _allCheckListsState = MutableStateFlow<UiState<List<CheckList>>>(UiState.Loading)
    val allCheckListsState: StateFlow<UiState<List<CheckList>>> = _allCheckListsState

    // StateFlow to hold trashed checklists
    private val _trashedNoteListsState = MutableStateFlow<UiState<List<CheckList>>>(UiState.Loading)
    val trashedNoteListsState: StateFlow<UiState<List<CheckList>>> = _trashedNoteListsState

    // StateFlow to hold favorite checklists
    private val _favoriteNoteListsState =
        MutableStateFlow<UiState<List<CheckList>>>(UiState.Loading)
    val favoriteNoteListsState: StateFlow<UiState<List<CheckList>>> = _favoriteNoteListsState

    // Initialize by loading all states
    init {
        refreshAllStates()
    }

    /**
     * Generic function to load a list using the provided use case
     * and update the given StateFlow with UiState (Loading, Success, Error)
     */
    private fun loadLists(
        useCase: () -> kotlinx.coroutines.flow.Flow<List<CheckList>>,
        stateFlow: MutableStateFlow<UiState<List<CheckList>>>
    ) = viewModelScope.launch {
        try {
            stateFlow.value = UiState.Loading       // Set loading state
            delay(400)                              // Optional delay for smoother UI

            // Collect latest items from the Flow
            useCase().collectLatest { lists ->
                stateFlow.value = UiState.Success(lists) // Update with success state
            }
        } catch (e: Exception) {
            Log.e(TAG, "loadLists error", e)
            stateFlow.value = UiState.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    // Refresh all checklist states (all, favorite, trashed)
    private fun refreshAllStates() {
        loadLists({ getAllListsUseCase() }, _allCheckListsState)
        loadLists({ getFavoriteListsUseCase() }, _favoriteNoteListsState)
        loadLists({ getTrashListsUseCase() }, _trashedNoteListsState)
    }

    /**
     * Helper function to perform an action on a checklist
     * and then refresh all states, catching any exceptions
     */
    private fun performAction(action: suspend () -> Unit) = viewModelScope.launch {
        try {
            action()
            refreshAllStates()
        } catch (e: Exception) {
            Log.e(TAG, "performAction error", e)
        }
    }

    /**
     * Add a new checklist and return the generated ID via onComplete callback
     */
    fun addCheckList(checkList: CheckList, onComplete: (Long) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val id = insertListUseCase(checkList)
                onComplete(id)        // Return the inserted ID
                refreshAllStates()    // Refresh all checklist states
            } catch (e: Exception) {
                Log.e(TAG, "addNoteList error", e)
            }
        }
    }

    /** Update an existing checklist */
    fun editCheckList(checkList: CheckList) = performAction {
        updateListUseCase(checkList)
    }

    /** Move a checklist to trash by marking it as trashed */
    fun moveCheckListToTrash(checkList: CheckList) = performAction {
        updateListUseCase(checkList.copy(isTrashed = true))
    }

    /** Restore a trashed checklist */
    fun restoreCheckList(checkList: CheckList) = performAction {
        restoreListUseCase(checkList.copy(isTrashed = false))
    }

    /** Toggle favorite status of a checklist */
    fun toggleFavorite(checkList: CheckList) = performAction {
        updateListUseCase(checkList.copy(isFavorite = !checkList.isFavorite))
    }

    /** Permanently delete a checklist */
    fun deleteCheckListForever(checkList: CheckList) = performAction {
        deleteListUseCase(checkList)
    }

    /** Delete multiple checklists forever */
    fun deleteCheckListsForever(lists: List<CheckList>) = viewModelScope.launch {
        lists.forEach { deleteCheckListForever(it) }
    }



}
