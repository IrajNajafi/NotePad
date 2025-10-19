package com.irajnajafi1988gmail.notepad.data.repository.datastore

import com.irajnajafi1988gmail.notepad.domain.model.CalendarType
import kotlinx.coroutines.flow.Flow

interface DateRepository {
    fun getCalendarType(): Flow<CalendarType>
    suspend fun saveCalendarType(calendarType: CalendarType)

}