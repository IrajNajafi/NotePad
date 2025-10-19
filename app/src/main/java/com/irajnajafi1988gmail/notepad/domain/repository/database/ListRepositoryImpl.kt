package com.irajnajafi1988gmail.notepad.domain.repository.database

import com.irajnajafi1988gmail.notepad.data.local.db.dao.ListDao
import com.irajnajafi1988gmail.notepad.data.local.mapper.toDomain
import com.irajnajafi1988gmail.notepad.data.local.mapper.toEntity
import com.irajnajafi1988gmail.notepad.data.repository.database.ListRepository
import com.irajnajafi1988gmail.notepad.domain.model.CheckList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ListRepositoryImpl @Inject constructor(
    private val listDao: ListDao
) : ListRepository {
    override suspend fun insertList(noteList: CheckList): Long {
        return listDao.insertList(noteList.toEntity())
    }

    override suspend fun updateList(noteList: CheckList) {
        return listDao.updateList(noteList.toEntity())
    }

    override suspend fun deleteList(NoteList: CheckList) {
        return listDao.deleteList(NoteList.toEntity())
    }


    override fun getListBydId(listId: Long): Flow<CheckList?> {
        return listDao.getListBydId(listId).map { it?.toDomain() }
    }

    override fun getAllList(): Flow<List<CheckList>> {
        return listDao.getAllList().map { list ->
            list.map { it.toDomain() }
        }


    }

    override fun getTrashList(): Flow<List<CheckList>> {
        return listDao.getTrashList().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getFavoriteList(): Flow<List<CheckList>> {
        return listDao.getFavoriteList().map { list ->
            list.map { it.toDomain() }
        }

    }
}