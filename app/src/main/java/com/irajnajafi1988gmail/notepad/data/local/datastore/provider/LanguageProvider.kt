package com.irajnajafi1988gmail.notepad.data.local.datastore.provider

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.irajnajafi1988gmail.notepad.data.local.datastore.prefKeys.LanguagePrefKeys

val Context.LanguageDataStore by preferencesDataStore(name = LanguagePrefKeys.LANGUAGE_NAME)