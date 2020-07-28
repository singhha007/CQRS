package com.servicetitan.android.platform.writer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.servicetitan.android.platform.sourcer.Event
import com.servicetitan.android.platform.sourcer.EventService
import com.servicetitan.android.platform.writer.db.CommandDao
import com.servicetitan.android.platform.writer.db.EventDao
import com.servicetitan.android.platform.writer.helper.DASH
import com.servicetitan.android.platform.writer.helper.NEW_LINE
import com.servicetitan.android.platform.writer.model.Command
import com.servicetitan.android.platform.writer.model.Job
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_writer.*
import java.util.*
import javax.inject.Inject

private const val TAG = "WriterFragment"
class WriterFragment : Fragment() {

    @Inject
    lateinit var commandDao: CommandDao

    @Inject
    lateinit var eventDao: EventDao

    private var eventService: EventService? = null
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WriterManager.writerComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_writer, container, false).also {
            connectToService()
            createJob.setOnClickListener { createJob() }
            updateJob.setOnClickListener { updateJob() }
            deleteJob.setOnClickListener { deleteJob() }
        }

    private fun createJob() {
        val uuid = UUID.randomUUID().toString().split(DASH)
        val job = Job(uuid.first(), jobName.text.toString())
        val command = Command(uuid[1], "CREATE", Date(), job)
        val event = Event(uuid.last(), "CREATE JOB", Date(), job)

        commandDao.insert(command).andThen(eventDao.insert(event))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Log.d(TAG, "Command/Event Insert Failed") }
            .subscribe { eventService?.notify(event).also { updateLog() } }
            .addTo(disposable)
    }

    private fun updateJob() {

    }

    private fun deleteJob() {
        //commandDao.getCommandByType()
    }

    private fun updateLog() {
        commandDao.getCommands()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Log.d(TAG, "Error Getting Command Log") }
            .subscribe { log.text = it.joinToString(NEW_LINE) { command -> command.toString() } }
            .addTo(disposable)
    }

    private fun connectToService() {
        activity?.bindService(Intent(requireContext(), EventService::class.java),
            serviceConnection(), Context.BIND_AUTO_CREATE)
    }

    private fun serviceConnection(): ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            eventService = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            eventService = (service as EventService.EventServiceBinder).service
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}