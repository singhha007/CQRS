package com.servicetitan.android.platform.cqrs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.servicetitan.android.platform.writer.WriterManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.writerFragment, WriterManager.provideWriterFragment()).commit()
    }
}