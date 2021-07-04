package com.dscvit.werk.ui.tasks

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dscvit.werk.R
import com.dscvit.werk.databinding.ActivityTaskDescriptionBinding
import com.dscvit.werk.models.task.Task
import com.dscvit.werk.service.TimerService
import com.dscvit.werk.ui.adapter.MemberDialogAdapter
import com.dscvit.werk.ui.session.ParticipantsViewModel
import com.dscvit.werk.ui.utils.buildLoader
import com.dscvit.werk.ui.utils.showErrorSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TaskDescriptionActivity : AppCompatActivity() {
    private val TAG: String = this.javaClass.simpleName

    private lateinit var binding: ActivityTaskDescriptionBinding
    private lateinit var membersAdapter: MemberDialogAdapter
    private lateinit var membersDialogBox: Dialog
    private lateinit var task: Task

    private lateinit var statusReceiver: BroadcastReceiver
    private lateinit var timerReceiver: BroadcastReceiver
    private var isTimerRunning = false

    private val viewModel: ParticipantsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDescriptionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navArgs by navArgs<TaskDescriptionActivityArgs>()
        task = navArgs.task

        getTimerStatus(task.taskId, task.title)

        binding.appBarTitle.text = task.title
        binding.descBody.text = task.description

        binding.descPoints.text = "Points: ${task.points}"

        if (task.assigned != null) {
            binding.assignedPhoto.load(task.assigned?.avatar)
            binding.assignedName.text = task.assigned?.name
            binding.assignedEmail.text = task.assigned?.email
        } else {
            binding.assignedPhoto.visibility = View.GONE
            binding.assignedName.visibility = View.GONE
            binding.assignedEmail.visibility = View.GONE
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.changeAssignedButton.setOnClickListener {
            membersDialogBox = Dialog(this)
            membersDialogBox.setContentView(R.layout.member_dialog_box)
            membersAdapter = MemberDialogAdapter()

            val recyclerView =
                membersDialogBox.findViewById<RecyclerView>(R.id.members_recycler_view)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = membersAdapter

            val loader = buildLoader()

            viewModel.getParticipants()

            lifecycleScope.launchWhenResumed {
                viewModel.participants.collect { event ->
                    when (event) {
                        is ParticipantsViewModel.GetParticipantsEvent.Success -> {
                            loader.hide()
                            val participants = event.participantsResponse.participants.filter {
                                it.joined
                            }
                            membersAdapter.setParticipants(participants)
                            membersDialogBox.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            membersDialogBox.show()
                        }
                        is ParticipantsViewModel.GetParticipantsEvent.Loading -> {
                            Log.d(TAG, "LOADING...")
                            loader.show()
                        }
                        is ParticipantsViewModel.GetParticipantsEvent.Failure -> {
                            loader.hide()
                            Log.d(TAG, event.errorMessage)
                            view.showErrorSnackBar(event.errorMessage)
                        }
                        else -> {
                        }
                    }
                }
            }
        }

        binding.toggleButton.setOnClickListener {
            if (isTimerRunning) pauseTimer(task.taskId, task.title) else startTimer(
                task.taskId,
                task.title
            )
        }
    }

    override fun onStart() {
        super.onStart()

        // Receiving task status from service
        val statusFiler = IntentFilter()
        statusFiler.addAction("TaskStatus${task.taskId}")
        statusReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                val isRunning = p1?.getBooleanExtra("TaskStatus", false)!!
                isTimerRunning = isRunning
                Log.d("Timer", "ACTIVITY Task ${task.taskId} STATUS: $isRunning")
                val text = if (isRunning) "Pause" else "Start"
                binding.toggleButton.text = text

                val timeElapsed = p1.getIntExtra("TaskTimeElapsed", 0)
                binding.timerText.text = timeElapsed.toString()
            }
        }
        registerReceiver(statusReceiver, statusFiler)

        // Receiving time values from service
        val timerFilter = IntentFilter()
        timerFilter.addAction("TaskTimer${task.taskId}")
        timerReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                val time = p1?.getIntExtra("TimeElapsed", 0)
                binding.timerText.text = time.toString()
            }
        }
        registerReceiver(timerReceiver, timerFilter)
    }

    override fun onPause() {
        super.onPause()

        unregisterReceiver(statusReceiver)
        unregisterReceiver(timerReceiver)
    }

    private fun startTimer(taskID: Int, taskName: String) {
        val timerService = Intent(this, TimerService::class.java)
        timerService.putExtra("TaskID", taskID)
        timerService.putExtra("TaskName", taskName)
        timerService.putExtra("Action", TimerService.START)
        startService(timerService)
    }

    private fun pauseTimer(taskID: Int, taskName: String) {
        val timerService = Intent(this, TimerService::class.java)
        timerService.putExtra("TaskID", taskID)
        timerService.putExtra("TaskName", taskName)
        timerService.putExtra("Action", TimerService.PAUSE)
        startService(timerService)
    }

    private fun getTimerStatus(taskID: Int, taskName: String) {
        val timerService = Intent(this, TimerService::class.java)
        timerService.putExtra("TaskID", taskID)
        timerService.putExtra("TaskName", taskName)
        timerService.putExtra("Action", TimerService.GET_STATUS)
        startService(timerService)
    }
}