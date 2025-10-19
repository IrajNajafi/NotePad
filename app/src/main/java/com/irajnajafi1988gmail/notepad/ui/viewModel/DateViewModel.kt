package com.irajnajafi1988gmail.notepad.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irajnajafi1988gmail.notepad.domain.model.CalendarType
import com.irajnajafi1988gmail.notepad.domain.usecase.dateUseCase.GetCalendarTypeUseCase
import com.irajnajafi1988gmail.notepad.domain.usecase.dateUseCase.SaveDateUseCase
import com.irajnajafi1988gmail.notepad.domain.utils.AppDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

// Hilt annotation for dependency injection
@HiltViewModel
class DateViewModel @Inject constructor(
    private val appDate: AppDate,                         // Utility class for date formatting and conversions
    private val getCalendarTypeUseCase: GetCalendarTypeUseCase, // Use case to get the active calendar type (e.g., Gregorian, Persian)
    private val saveDateUseCase: SaveDateUseCase           // Use case to save selected date (currently not used in this snippet)
) : ViewModel() {

    /**
     * StateFlow holding the currently active calendar type
     * Uses stateIn to convert a Flow to StateFlow with initial value GREGORIAN
     */
    private val _activeCalendarType: StateFlow<CalendarType> = getCalendarTypeUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Keep flow active while observed
            initialValue = CalendarType.GREGORIAN
        )

    // Public read-only StateFlow for active calendar type
    val activeCalendarType: StateFlow<CalendarType> = _activeCalendarType

    /**
     * StateFlow representing today's date as a formatted string
     * Updates automatically when the active calendar type changes
     */
    val today: StateFlow<String> = _activeCalendarType
        .map { calendarType ->
            appDate.getFormattedDate(calendarType = calendarType) // Format date according to selected calendar
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "" // Initial value is empty string
        )

    /**
     * StateFlow representing today's date in a format suitable for saving
     * Updates automatically when the active calendar type changes
     */
    val todayForSave: StateFlow<String> = _activeCalendarType
        .map { calendarType ->
            appDate.getDateForSave(calendarType = calendarType) // Save-friendly format
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    /**
     * Utility function to get formatted date string from a timestamp
     * Returns empty string if timestamp is null, invalid, or if an exception occurs
     */
    fun getFormattedDate(timestamp: Long?): String {
        return try {
            if (timestamp == null || timestamp <= 0L) return ""
            appDate.getFormattedDate(
                timestamp = timestamp,
                calendarType = _activeCalendarType.value // Use currently active calendar type
            )
        } catch (e: Exception) {
            "" // Return empty string on error
        }
    }

}
