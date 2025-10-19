package com.irajnajafi1988gmail.notepad.data.local.datastore.provider

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.irajnajafi1988gmail.notepad.data.local.datastore.prefKeys.ThemePrefKeys

val Context.ThemeDataStore by preferencesDataStore(name = ThemePrefKeys.DARK_MODE_NAME)