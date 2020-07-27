package com.servicetitan.android.platform.sourcer

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import io.reactivex.subjects.PublishSubject

private const val TAG = "EventService"

class EventService : Service() {

    private val eventNotifier = PublishSubject.create<Unit>()
    private val eventListener = eventNotifier.hide()

    private val eventServiceBinder: IBinder = EventServiceBinder()

    inner class EventServiceBinder: Binder() {
        val service = this@EventService
    }

    override fun onBind(intent: Intent): IBinder = eventServiceBinder

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service Created")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service Disconnected")
    }
}
