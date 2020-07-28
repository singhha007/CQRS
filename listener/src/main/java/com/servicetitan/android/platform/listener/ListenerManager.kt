package com.servicetitan.android.platform.listener

import android.content.Context
import com.servicetitan.android.platform.listener.di.DaggerListenerComponent
import com.servicetitan.android.platform.listener.di.ListenerComponent

object ListenerManager {

    lateinit var listenerComponent: ListenerComponent

    fun initialize(context: Context) {
        listenerComponent = DaggerListenerComponent.factory().create(context)
    }

    fun provideListenerFragment() = ListenerFragment()
}