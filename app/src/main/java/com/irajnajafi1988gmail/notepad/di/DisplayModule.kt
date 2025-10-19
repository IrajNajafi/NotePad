package com.irajnajafi1988gmail.notepad.di

import com.irajnajafi1988gmail.notepad.data.repository.datastore.DisplayRepository
import com.irajnajafi1988gmail.notepad.domain.repository.database.DisplayRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DisplayModule {
    @Binds
    @Singleton
    abstract fun bindDisplayRepository(
        impl: DisplayRepositoryImpl
    ): DisplayRepository


}