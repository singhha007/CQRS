package com.servicetitan.android.platform.listener.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.servicetitan.android.platform.sourcer.Event
import com.servicetitan.android.platform.listener.model.Command

@Database(entities = [Command::class, Event::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ListenerDatabase: RoomDatabase() {
    abstract fun commandDao(): CommandDao
    abstract fun eventDao(): EventDao
}