package com.irajnajafi1988gmail.notepad.domain.usecase.dateUseCase

import com.irajnajafi1988gmail.notepad.data.repository.datastore.DateRepository
import com.irajnajafi1988gmail.notepad.data.repository.datastore.DisplayRepository
import com.irajnajafi1988gmail.notepad.domain.model.CalendarType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCalendarTypeUseCase @Inject constructor(
    private val repository: DateRepository
) {
    operator fun invoke(): Flow<CalendarType> = repository.getCalendarType()


}