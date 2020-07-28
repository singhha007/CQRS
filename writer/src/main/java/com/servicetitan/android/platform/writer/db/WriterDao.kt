package com.servicetitan.android.platform.writer.db

import androidx.room.*
import com.servicetitan.android.platform.sourcer.Event
import com.servicetitan.android.platform.writer.model.Command
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: T): Completable

    @Delete
    fun delete(item: T): Completable
}

@Dao
interface CommandDao: BaseDao<Command> {

    @Query("SELECT * FROM COMMAND")
    fun getCommands(): Single<List<Command>>

    @Query("SELECT * FROM COMMAND WHERE commandId LIKE :commandId LIMIT 1")
    fun getCommandByID(commandId: String): Single<Command>


    @Query("SELECT * FROM COMMAND WHERE commandType LIKE :commandType LIMIT 1")
    fun getCommandByType(commandType: String): Single<Command>
}

@Dao
interface EventDao: BaseDao<Event> {

    @Query("SELECT * FROM EVENT WHERE eventId LIKE :eventId LIMIT 1")
    fun getEventByID(eventId: String): Single<Event>


    @Query("SELECT * FROM EVENT WHERE eventType LIKE :eventType LIMIT 1")
    fun getEventByType(eventType: String): Single<Event>
}