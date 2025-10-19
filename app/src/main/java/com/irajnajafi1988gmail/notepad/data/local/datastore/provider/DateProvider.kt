package com.irajnajafi1988gmail.notepad.data.local.datastore.provider

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.irajnajafi1988gmail.notepad.data.local.datastore.prefKeys.DatePreferencesKeys

val Context.dateDataStore by preferencesDataStore(name = DatePreferencesKeys.CALENDAR_TYPE)