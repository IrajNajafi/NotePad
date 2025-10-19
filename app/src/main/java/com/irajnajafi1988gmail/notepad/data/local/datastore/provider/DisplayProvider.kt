package com.irajnajafi1988gmail.notepad.data.local.datastore.provider

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.irajnajafi1988gmail.notepad.data.local.datastore.prefKeys.DisplayPrefKeys


val Context.displayDataStore by preferencesDataStore(name = DisplayPrefKeys.DISPLAY_NAME)