package com.servicetitan.android.platform.writer.di

import android.content.Context
import androidx.room.Room
import com.servicetitan.android.platform.writer.db.WriterDatabase
import dagger.Module

private const val DATABASE_NAME = "writer-database"

@Module
class WriterModule {

    fun provideWriterDatabase(context: Context) =
        Room.databaseBuilder(context,  WriterDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    fun provideCommandDao(writerDatabase: WriterDatabase) = writerDatabase.commandDao()

    fun provideEventDao(writerDatabase: WriterDatabase) = writerDatabase.eventDao()
}