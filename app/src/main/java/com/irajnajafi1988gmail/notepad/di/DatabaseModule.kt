package com.irajnajafi1988gmail.notepad.di

import android.content.Context
import androidx.room.Room
import com.irajnajafi1988gmail.notepad.data.local.db.MyDatabase
import com.irajnajafi1988gmail.notepad.data.local.db.dao.ListDao
import com.irajnajafi1988gmail.notepad.data.local.db.dao.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMyDatabase(@ApplicationContext context: Context): MyDatabase {
        return Room.databaseBuilder(
            context,
            MyDatabase::class.java,
            MyDatabase.DBNAME
        ).build()

    }

    @Provides
    fun provideNoteDao(myDatabase: MyDatabase): NoteDao {
        return myDatabase.noteDao()
    }

    @Provides
    fun provideListDao(myDatabase: MyDatabase): ListDao {
        return myDatabase.listDao()
    }
}