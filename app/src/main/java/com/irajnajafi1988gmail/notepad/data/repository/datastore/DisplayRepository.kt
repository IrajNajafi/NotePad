package com.irajnajafi1988gmail.notepad.data.repository.datastore

import kotlinx.coroutines.flow.Flow

interface DisplayRepository {

    val getDisplay: Flow<Boolean>
    suspend fun saveDisplay(display: Boolean)
    suspend fun toggleDisplay()

}