package com.irajnajafi1988gmail.notepad.di

import com.irajnajafi1988gmail.notepad.data.repository.datastore.ThemeRepository
import com.irajnajafi1988gmail.notepad.domain.repository.datastore.ThemeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

abstract class ThemeModule {
    @Binds
    @Singleton
    abstract fun bindThemeRepository(
        impl: ThemeRepositoryImpl
    ): ThemeRepository
}