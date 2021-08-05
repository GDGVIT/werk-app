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
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dscvit.werk.R
import com.dscvit.werk.databinding.ActivityTaskDescriptionBinding
import com.dscvit.werk.models.task.Task
import com.dscvit.werk.service.TimerService
import com.dscvit.werk.ui.adapter.MemberDialogAdapter
import com.dscvit.werk.ui.overview.SessionsOverviewFragmentDirections
import com.dscvit.werk.ui.session.ParticipantsViewModel
import com.dscvit.werk.ui.utils.OnItemClickListener
import com.dscvit.werk.ui.utils.addOnItemClickListener
import com.dscvit.werk.ui.utils.buildLoader
import com.dscvit.werk.ui.utils.showErrorSnackBar
import com.dscvit.werk.util.APP_PREF
import com.dscvit.werk.util.PREF_TOKEN
import com.dscvit.werk.util.PrefHelper
import com.dscvit.werk.util.PrefHelper.set
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
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
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDescriptionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navArgs by navArgs<TaskDescriptionActivityArgs>()
        task = navArgs.task

        getTimerStatus(task.taskId, task.title)

        taskViewModel.getTaskDetails(task.taskId)

        binding.appBarTitle.text = task.title
        binding.descBody.text = task.description

        binding.descPoints.text = "Points: ${task.points}"

        if (task.assigned != null && task.status != "completed") {
            if (task.assignedTo == taskViewModel.getUserID()) {
                binding.circularClock.visibility = View.VISIBLE
            } else {
                binding.circularClock.visibility = View.GONE
            }
        } else {
            binding.circularClock.visibility = View.GONE
        }

        binding.moreOptionsMenu.setOnClickListener {
            val popup = PopupMenu(this, binding.moreOptionsMenu)
            popup.menuInflater.inflate(R.menu.more_actions_task_menu, popup.menu)

            popup.setOnMenuItemClickListener {
                if (it.itemId == R.id.terminate_task) {
                    taskViewModel.terminateTheTask(task.taskId)
                    return@setOnMenuItemClickListener true
                } else {
                    Toast.makeText(this, it.itemId.toString(), Toast.LENGTH_SHORT)
                        .show()
                    return@setOnMenuItemClickListener true
                }
            }

            popup.show()
        }

        if (task.assigned != null) {
            binding.assignedPhoto.load(task.assigned?.avatar)
            binding.assignedName.text = task.assigned?.name
            binding.assignedEmail.text = task.assigned?.email
            binding.changeAssignedButton.visibility = View.GONE
        } else {
            binding.assignedPhoto.visibility = View.GONE
            binding.assignedName.visibility = View.GONE
            binding.assignedEmail.visibility = View.GONE
            binding.changeAssignedButton.visibility = View.VISIBLE
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        val loader = buildLoader()

        binding.changeAssignedButton.setOnClickListener {
            membersDialogBox = Dialog(this)
            membersDialogBox.setContentView(R.layout.member_dialog_box)
            membersAdapter = MemberDialogAdapter()

            val recyclerView =
                membersDialogBox.findViewById<RecyclerView>(R.id.members_recycler_view)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = membersAdapter

            recyclerView.addOnItemClickListener(object : OnItemClickListener {
                override fun onItemClicked(position: Int, view: View) {
                    val participant = membersAdapter.getParticipants(position)
                    membersDialogBox.dismiss()
                    viewModel.assignAParticipant(participant, task.taskId)
                }
            })

            viewModel.getParticipants()

            lifecycleScope.launchWhenCreated {
                viewModel.assignParticipant.collect { event ->
                    when (event) {
                        is ParticipantsViewModel.AssignParticipantEvent.Success -> {
                            Log.d(TAG, "Sucesss...${event.participant}")

                            binding.assignedPhoto.visibility = View.VISIBLE
                            binding.assignedName.visibility = View.VISIBLE
                            binding.assignedEmail.visibility = View.VISIBLE

                            binding.assignedName.text = event.participant.name
                            binding.assignedEmail.text = event.participant.email
                            binding.assignedPhoto.load(event.participant.avatar)

                            loader.hide()

                            finish()
                        }
                        is ParticipantsViewModel.AssignParticipantEvent.Loading -> {
                            Log.d(TAG, "LOADING...")
                            loader.show()
                        }
                        is ParticipantsViewModel.AssignParticipantEvent.Failure -> {
                            Log.d(TAG, event.errorMessage)

                            view.showErrorSnackBar(event.errorMessage)

                            loader.hide()
                        }
                        else -> {
                        }
                    }
                }
            }

            lifecycleScope.launchWhenCreated {
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

        lifecycleScope.launchWhenCreated {
            taskViewModel.terminateTask.collect { event ->
                when (event) {
                    is TaskViewModel.ChangeStatusEvent.Success -> {
                        loader.hide()
                        Log.d(TAG, "SUCCESS")
                        taskViewModel.removeTask(task.taskId)
                        finish()
                    }
                    is TaskViewModel.ChangeStatusEvent.Loading -> {
                        Log.d(TAG, "LOADING...")
                        loader.show()
                    }
                    is TaskViewModel.ChangeStatusEvent.Failure -> {
                        loader.hide()
                        Log.d(TAG, event.errorMessage)
                        view.showErrorSnackBar(event.errorMessage)
                    }
                    else -> {
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            taskViewModel.taskDetails.collect { event ->
                when (event) {
                    is TaskViewModel.GetTaskDetailsEvent.Success -> {
                        loader.hide()
                        if (!isTimerRunning) {
                            setElapsedTime(
                                task.taskId,
                                task.title,
                                event.taskDetailsResponse.elapsedSecs
                            )
                        }
                        Log.d(TAG, "SUCCESS")
                    }
                    is TaskViewModel.GetTaskDetailsEvent.Loading -> {
                        Log.d(TAG, "LOADING...")
                        loader.show()
                    }
                    is TaskViewModel.GetTaskDetailsEvent.Failure -> {
                        loader.hide()
                        Log.d(TAG, event.errorMessage)
                        view.showErrorSnackBar(event.errorMessage)
                    }
                    else -> {
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            taskViewModel.submitTask.collect { event ->
                when (event) {
                    is TaskViewModel.SubmitTaskEvent.Success -> {
                        loader.hide()
                        Log.d(TAG, "TASK SUBMITTED")
                        finish()
                    }
                    is TaskViewModel.SubmitTaskEvent.Loading -> {
                        Log.d(TAG, "LOADING...")
                        loader.show()
                    }
                    is TaskViewModel.SubmitTaskEvent.Failure -> {
                        loader.hide()
                        Log.d(TAG, event.errorMessage)
                        view.showErrorSnackBar(event.errorMessage)
                    }
                    else -> {
                    }
                }
            }
        }

        binding.toggleButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                binding.toggleButton.startAnimation()
                if (isTimerRunning) taskViewModel.pauseTask(task.taskId) else taskViewModel.startTask(
                    task.taskId
                )
                binding.toggleButton.revertAnimation {
                    if (isTimerRunning) pauseTimer(task.taskId, task.title) else startTimer(
                        task.taskId,
                        task.title
                    )
                }

            }
        }

        binding.doneButton.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Submit Task? 📈")
                .setMessage("Once you submit the task, you won't be able to restart the task again.")
                .setPositiveButton("Submit") { dialog, _ ->
                    dialog.dismiss()
                    finishTask(task.taskId, task.title)
                    taskViewModel.submitTask(task.taskId, 5)
                }
                .setNegativeButton("Not done yet") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
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
                val minutes: Int = timeElapsed / 60
                val seconds: Int = timeElapsed % 60
                binding.timerText.text = "${"%02d".format(minutes)} : ${"%02d".format(seconds)}"
            }
        }
        registerReceiver(statusReceiver, statusFiler)

        // Receiving time values from service
        val timerFilter = IntentFilter()
        timerFilter.addAction("TaskTimer${task.taskId}")
        timerReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                val time = p1?.getIntExtra("TimeElapsed", 0)
                val minutes: Int = time?.div(60) ?: 0
                val seconds: Int = time?.rem(60) ?: 0
                binding.timerText.text = "${"%02d".format(minutes)} : ${"%02d".format(seconds)}"
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

    private fun finishTask(taskID: Int, taskName: String) {
        val timerService = Intent(this, TimerService::class.java)
        timerService.putExtra("TaskID", taskID)
        timerService.putExtra("TaskName", taskName)
        timerService.putExtra("Action", TimerService.DONE)
        startService(timerService)
    }

    private fun setElapsedTime(taskID: Int, taskName: String, elapsedTime: Int) {
        val timerService = Intent(this, TimerService::class.java)
        timerService.putExtra("TaskID", taskID)
        timerService.putExtra("TaskName", taskName)
        timerService.putExtra("Action", TimerService.SET_ELAPSED_TIME)
        timerService.putExtra("TaskElapsedTime", elapsedTime)
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