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
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.werk.R
import com.dscvit.werk.databinding.ActivityTaskDescriptionBinding
import com.dscvit.werk.service.TimerService
import com.dscvit.werk.ui.adapter.MemberDialogAdapter

class TaskDescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskDescriptionBinding
    private lateinit var membersAdapter: MemberDialogAdapter
    private lateinit var membersDialogBox: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDescriptionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navArgs by navArgs<TaskDescriptionActivityArgs>()
        val task = navArgs.task

        binding.appBarTitle.text = task.title
        binding.descBody.text = task.description

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

            membersDialogBox.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            membersDialogBox.show()
        }

        binding.toggleButton.setOnClickListener {
            startTimer()
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction("TaskTimer${task.id}")

        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                val time = p1?.getIntExtra("TimeElapsed", 0)
                Log.d("Timer", "ACTIVITY Task ${task.id}: $time")
            }
        }

        registerReceiver(broadcastReceiver, intentFilter)
    }

    private fun startTimer() {
        val intentService = Intent(this, TimerService::class.java)
        val taskID = 1
        intentService.putExtra("TaskID", taskID)
        startService(intentService)
    }
}