package com.servicetitan.android.platform.listener.di

import android.content.Context
import androidx.room.Room
import com.servicetitan.android.platform.listener.db.ListenerDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

private const val DATABASE_NAME = "listener-database"

@Module
class ListenerModule {

    @Provides
    @Singleton
    fun provideWriterDatabase(context: Context) =
        Room.databaseBuilder(context,  ListenerDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideCommandDao(listenerDatabase: ListenerDatabase) = listenerDatabase.commandDao()

    @Provides
    @Singleton
    fun provideEventDao(listenerDatabase: ListenerDatabase) = listenerDatabase.eventDao()
}