package com.servicetitan.android.platform.writer.di

import android.content.Context
import com.servicetitan.android.platform.writer.WriterFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [WriterModule::class])
interface WriterComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): WriterComponent
    }

    fun inject(writerFragment: WriterFragment)
}