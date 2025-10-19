package com.irajnajafi1988gmail.notepad.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irajnajafi1988gmail.notepad.domain.usecase.listusecase.GetTrashListsUseCase
import com.irajnajafi1988gmail.notepad.domain.usecase.listusecase.GetFavoriteListsUseCase
import com.irajnajafi1988gmail.notepad.domain.usecase.noteusecase.GetTrashNotesUseCase
import com.irajnajafi1988gmail.notepad.domain.usecase.noteusecase.GetFavoriteNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

// Hilt annotation for dependency injection
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTrashNotesUseCase: GetTrashNotesUseCase,       // Use case to get trashed notes
    private val getTrashListsUseCase: GetTrashListsUseCase,       // Use case to get trashed checklists
    private val getFavoriteNotesUseCase: GetFavoriteNotesUseCase, // Use case to get favorite notes
    private val getFavoriteListsUseCase: GetFavoriteListsUseCase  // Use case to get favorite checklists
) : ViewModel() {

    /**
     * StateFlow representing the total number of trashed items (notes + checklists)
     * - Uses combine to sum the sizes of trashed notes and trashed lists
     * - stateIn converts the Flow to StateFlow with initial value 0
     */
    val totalTrashCount: StateFlow<Int> = combine(
        getTrashNotesUseCase().map { it.size }, // Map list of trashed notes to its size
        getTrashListsUseCase().map { it.size }  // Map list of trashed checklists to its size
    ) { noteCount, listCount ->
        noteCount + listCount // Sum of trashed notes and checklists
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // Keep active while observed
        initialValue = 0
    )

    /**
     * StateFlow representing the total number of favorite items (notes + checklists)
     * - Uses combine to sum the sizes of favorite notes and favorite lists
     * - stateIn converts the Flow to StateFlow with initial value 0
     */
    val totalFavoriteCount: StateFlow<Int> = combine(
        getFavoriteNotesUseCase().map { it.size }, // Map list of favorite notes to its size
        getFavoriteListsUseCase().map { it.size }  // Map list of favorite checklists to its size
    ) { noteCount, listCount ->
        noteCount + listCount // Sum of favorite notes and checklists
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // Keep active while observed
        initialValue = 0
    )
}
