package com.irajnajafi1988gmail.notepad.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.irajnajafi1988gmail.notepad.data.local.db.MyDatabase
import com.irajnajafi1988gmail.notepad.data.local.db.entities.ListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(listEntity: ListEntity): Long

    @Update
    suspend fun updateList(listEntity: ListEntity)

    @Delete
    suspend fun deleteList(listEntity: ListEntity)

    @Query("SELECT * FROM ${MyDatabase.LIST_TABLE} WHERE id = :listId LIMIT 1")
    fun getListBydId(listId: Long): Flow<ListEntity?>

    @Query("SELECT * FROM ${MyDatabase.LIST_TABLE} WHERE isTrashed = 0 ORDER BY timestamp DESC")
    fun getAllList(): Flow<List<ListEntity>>

    @Query("SELECT * FROM ${MyDatabase.LIST_TABLE} WHERE isTrashed = 1 ORDER BY timestamp DESC")
    fun getTrashList(): Flow<List<ListEntity>>

    @Query("SELECT * FROM ${MyDatabase.LIST_TABLE} WHERE isFavorite = 1 AND isTrashed = 0 ORDER BY timestamp DESC")
    fun getFavoriteList(): Flow<List<ListEntity>>
}
