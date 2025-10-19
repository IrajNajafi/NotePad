package com.irajnajafi1988gmail.notepad.di

import com.irajnajafi1988gmail.notepad.data.repository.datastore.DateRepository
import com.irajnajafi1988gmail.notepad.domain.repository.datastore.DateRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DateModule {
    @Binds
    @Singleton
    abstract fun bindDateRepository(
        impl: DateRepositoryImpl
    ): DateRepository
}