package com.servicetitan.android.platform.writer.di

import android.content.Context
import androidx.room.Room
import com.servicetitan.android.platform.writer.db.WriterDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

private const val DATABASE_NAME = "writer-database"

@Module
class WriterModule {

    @Provides
    @Singleton
    fun provideWriterDatabase(context: Context) =
        Room.databaseBuilder(context,  WriterDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideCommandDao(writerDatabase: WriterDatabase) = writerDatabase.commandDao()

    @Provides
    @Singleton
    fun provideEventDao(writerDatabase: WriterDatabase) = writerDatabase.eventDao()
}