package com.servicetitan.android.platform.cqrs

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.servicetitan.android.platform.sourcer.EventService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var eventService: EventService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindService(Intent(this, EventService::class.java), serviceConnection(), Context.BIND_AUTO_CREATE)

        secondActivity.setOnClickListener { startActivity(Intent(this, SecondActivity::class.java)) }
    }

    private fun serviceConnection(): ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            eventService = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            eventService = (service as EventService.EventServiceBinder).service
        }
    }
}