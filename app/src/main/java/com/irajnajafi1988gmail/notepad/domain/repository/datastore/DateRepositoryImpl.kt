package com.irajnajafi1988gmail.notepad.domain.repository.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.irajnajafi1988gmail.notepad.data.local.datastore.prefKeys.DatePreferencesKeys
import com.irajnajafi1988gmail.notepad.data.local.datastore.provider.dateDataStore
import com.irajnajafi1988gmail.notepad.data.repository.datastore.DateRepository
import com.irajnajafi1988gmail.notepad.domain.model.CalendarType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DateRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DateRepository {

    override fun getCalendarType(): Flow<CalendarType> = context.dateDataStore.data
        .map{preferences ->
            val code = preferences[DatePreferencesKeys.CALENDAR_KEY] ?: CalendarType.GREGORIAN.code
            CalendarType.fromCode(code)

    }

    override suspend fun saveCalendarType(calendarType: CalendarType) {
        context.dateDataStore.edit { prefs ->
            prefs[DatePreferencesKeys.CALENDAR_KEY] = calendarType.code
        }


    }

}