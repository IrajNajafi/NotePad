package com.irajnajafi1988gmail.notepad.di

import com.irajnajafi1988gmail.notepad.data.repository.datastore.LanguageRepository
import com.irajnajafi1988gmail.notepad.domain.repository.datastore.LanguageRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LanguageModule {
    @Binds
    @Singleton
    abstract fun bindLanguageRepository(
        impl: LanguageRepositoryImpl
    ): LanguageRepository
}