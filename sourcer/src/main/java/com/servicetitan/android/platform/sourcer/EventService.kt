package com.servicetitan.android.platform.sourcer

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

private const val TAG = "EventService"

class EventService : Service() {

    private val eventNotifier = PublishSubject.create<Event>()
    private val eventListener = eventNotifier.hide()

    private val eventServiceBinder: IBinder = EventServiceBinder()

    inner class EventServiceBinder: Binder() {
        val service = this@EventService
    }

    override fun onBind(intent: Intent): IBinder = eventServiceBinder

    fun notify(event: Event) {
        eventNotifier.onNext(event)
    }

    fun listenToEvents(): Observable<Event> = eventListener

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service Created")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service Disconnected")
    }
}
