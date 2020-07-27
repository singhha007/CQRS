package com.servicetitan.android.platform.writer.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.servicetitan.android.platform.sourcer.Event
import com.servicetitan.android.platform.writer.model.Command

@Database(entities = [Command::class, Event::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WriterDatabase: RoomDatabase() {
    abstract fun commandDao(): CommandDao
    abstract fun eventDao(): EventDao
}