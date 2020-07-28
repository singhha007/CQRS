package com.servicetitan.android.platform.writer

import android.content.Context
import com.servicetitan.android.platform.writer.di.DaggerWriterComponent
import com.servicetitan.android.platform.writer.di.WriterComponent

object WriterManager {

    lateinit var writerComponent: WriterComponent

    fun initialize(context: Context) {
        writerComponent = DaggerWriterComponent.factory().create(context)
    }

    fun provideWriterFragment() = WriterFragment()
}