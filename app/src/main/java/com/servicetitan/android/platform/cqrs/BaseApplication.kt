package com.servicetitan.android.platform.cqrs

import android.app.Application
import com.servicetitan.android.platform.listener.ListenerManager
import com.servicetitan.android.platform.writer.WriterManager

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        //Initialize Writer Library
        WriterManager.initialize(this)

        //Initialize Listener Library
        ListenerManager.initialize(this)
    }
}