package com.irajnajafi1988gmail.notepad.di

import android.content.Context
import com.irajnajafi1988gmail.notepad.domain.utils.AppDate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppDateModule {

    @Provides
    @Singleton
    fun provideAppDate(@ApplicationContext context: Context): AppDate {
        return AppDate(context)
    }
}