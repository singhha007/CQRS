package com.servicetitan.android.platform.listener.di

import android.content.Context
import com.servicetitan.android.platform.listener.ListenerFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ListenerModule::class])
interface ListenerComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ListenerComponent
    }

    fun inject(listenerFragment: ListenerFragment)
}