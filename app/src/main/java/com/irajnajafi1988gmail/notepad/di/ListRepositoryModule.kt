package com.irajnajafi1988gmail.notepad.di

import com.irajnajafi1988gmail.notepad.data.repository.database.ListRepository
import com.irajnajafi1988gmail.notepad.domain.repository.database.ListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ListRepositoryModule {
    @Binds
    abstract fun bindListRepository(
        impl:ListRepositoryImpl
    ):ListRepository
}