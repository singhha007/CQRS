package com.servicetitan.android.platform.writer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.servicetitan.android.platform.sourcer.Event
import com.servicetitan.android.platform.sourcer.EventService
import com.servicetitan.android.platform.sourcer.Job
import com.servicetitan.android.platform.writer.db.CommandDao
import com.servicetitan.android.platform.writer.db.EventDao
import com.servicetitan.android.platform.writer.helper.DASH
import com.servicetitan.android.platform.writer.helper.NEW_LINE
import com.servicetitan.android.platform.writer.model.Command
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_writer.*
import kotlinx.android.synthetic.main.fragment_writer.view.*
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_writer, container, false).also {
            connectToService()
            it.createJob.setOnClickListener { createJob() }
            it.updateJob.setOnClickListener { updateJob() }
            it.deleteJob.setOnClickListener { deleteJob() }
            it.log.movementMethod = ScrollingMovementMethod()
            updateLog()
        }

    private fun connectToService() {
        activity?.bindService(
            Intent(requireContext(), EventService::class.java),
            serviceConnection(), Context.BIND_AUTO_CREATE
        )
    }

    private fun serviceConnection(): ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            eventService = null
            progressBar.visibility = View.VISIBLE
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            progressBar.visibility = View.GONE
            eventService = (service as EventService.EventServiceBinder).service
            listenToEvents()
        }
    }

    private fun listenToEvents() {
        eventService?.listenToWriterEvents()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe {
                storeCommandAndEvent(createCommand(it.commandType, if(it.data != null) (it.data as Job) else null), it)
            }
            ?.addTo(disposable)
    }

    private fun createJob() {
        val uuid = UUID.randomUUID().toString().split(DASH)
        val job = Job(uuid.first(), jobName.text.toString())
        storeCommandAndEvent("CREATE", job)
    }

    private fun updateJob() {
        commandDao.getCommands()
            .filter { it.isNotEmpty() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Log.d(TAG, "Querying Commands Failed") }
            .subscribe {
                val job: Job? = it.first { command ->
                    command.data?.name?.equals(jobName.text.toString(), true) == true
                }.data?.apply {
                    location = "Glendale"
                    technician = "Hardeep"
                }
                storeCommandAndEvent("UPDATE", job)
            }
            .addTo(disposable)
    }

    private fun deleteJob() {
        commandDao.getCommands()
            .filter { it.isNotEmpty() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Log.d(TAG, "Querying Commands Failed") }
            .subscribe {
                if (it.isNotEmpty()) {
                    storeCommandAndEvent("DELETE", null)
                }
            }
            .addTo(disposable)
    }

    private fun updateLog() {
        commandDao.getCommands()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ log.text = it.joinToString(NEW_LINE) { command -> command.toString() } },
                { Log.d(TAG, "Error Getting Command Log") })
            .addTo(disposable)
    }

    private fun storeCommandAndEvent(action: String, job: Job?) {
        val event = createEvent(action, job)
        commandDao.insert(createCommand(action, job))
            .andThen(eventDao.insert(event))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Log.d(TAG, "Command/Event Create Failed") }
            .subscribe { eventService?.notifyListeners(event).also { updateLog() } }
            .addTo(disposable)
    }

    private fun storeCommandAndEvent(command: Command, event: Event) {
        commandDao.insert(command).andThen(eventDao.insert(event))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Log.d(TAG, "Command/Event Create Failed") }
            .subscribe { updateLog()  }
            .addTo(disposable)
    }

    private fun createCommand(action: String, job: Job?) =
        Command(UUID.randomUUID().toString().split(DASH)[1], action, Date(), job)

    private fun createEvent(action: String, job: Job?): Event =
        Event(UUID.randomUUID().toString().split(DASH).last(), action, "Writer: $action JOB", Date(), job)

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}