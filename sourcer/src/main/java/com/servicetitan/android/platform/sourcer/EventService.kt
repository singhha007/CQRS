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

    private val writerEventNotifier = PublishSubject.create<Event>()
    private val writerEventListener = writerEventNotifier.hide()

    private val listenerEventNotifier = PublishSubject.create<Event>()
    private val listenerEventListener = listenerEventNotifier.hide()

    private val eventServiceBinder: IBinder = EventServiceBinder()

    inner class EventServiceBinder: Binder() {
        val service = this@EventService
    }

    override fun onBind(intent: Intent): IBinder = eventServiceBinder

    fun notifyListeners(event: Event) {
        listenerEventNotifier.onNext(event)
    }

    fun notifyWriters(event: Event) {
        writerEventNotifier.onNext(event)
    }

    fun listenToWriterEvents(): Observable<Event> = writerEventListener

    fun listenToListenerEvents(): Observable<Event> = listenerEventListener

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service Created")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service Disconnected")
    }
}
