package com.irajnajafi1988gmail.notepad.data.repository.database

import com.irajnajafi1988gmail.notepad.domain.model.CheckList
import kotlinx.coroutines.flow.Flow

interface ListRepository {
    suspend fun insertList(noteList: CheckList): Long
    suspend fun updateList(noteList: CheckList)
    suspend fun deleteList(noteList: CheckList)
    fun getListBydId(listId: Long): Flow<CheckList?>
    fun getAllList(): Flow<List<CheckList>>
    fun getTrashList(): Flow<List<CheckList>>
    fun getFavoriteList(): Flow<List<CheckList>>
}