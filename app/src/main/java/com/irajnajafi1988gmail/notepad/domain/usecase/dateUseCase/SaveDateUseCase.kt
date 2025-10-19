package com.irajnajafi1988gmail.notepad.domain.usecase.dateUseCase

import com.irajnajafi1988gmail.notepad.data.repository.datastore.DateRepository
import com.irajnajafi1988gmail.notepad.domain.model.CalendarType
import javax.inject.Inject

class SaveDateUseCase @Inject constructor(
    private val repository: DateRepository
) {
    suspend operator fun invoke(calendarType: CalendarType) =
        repository.saveCalendarType(calendarType)
}