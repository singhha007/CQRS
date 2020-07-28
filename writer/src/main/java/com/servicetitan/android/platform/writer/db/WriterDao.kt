package com.servicetitan.android.platform.writer.db

import androidx.room.*
import com.servicetitan.android.platform.sourcer.Event
import com.servicetitan.android.platform.writer.model.Command
import io.reactivex.Completable
import io.reactivex.Observable

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: T): Completable

    @Delete
    fun delete(item: T): Completable
}

@Dao
interface CommandDao: BaseDao<Command> {

    @Query("SELECT * FROM COMMAND")
    fun getCommands(): Observable<List<Command>>

    @Query("SELECT * FROM COMMAND WHERE commandId LIKE :commandId LIMIT 1")
    fun getCommandByID(commandId: String): Observable<Command>


    @Query("SELECT * FROM COMMAND WHERE commandType LIKE :commandType LIMIT 1")
    fun getCommandByType(commandType: String): Observable<Command>
}

@Dao
interface EventDao: BaseDao<Event> {

    @Query("SELECT * FROM EVENT WHERE eventId LIKE :eventId LIMIT 1")
    fun getEventByID(eventId: String): Observable<Event>


    @Query("SELECT * FROM EVENT WHERE eventType LIKE :eventType LIMIT 1")
    fun getEventByType(eventType: String): Observable<Event>
}