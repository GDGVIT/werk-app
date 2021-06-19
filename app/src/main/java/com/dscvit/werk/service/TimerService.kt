package com.dscvit.werk.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.*
import kotlin.collections.HashMap

class TimerService : Service() {
    companion object {
        const val CHANNEL_ID = "Werk_Timer_Notifications"
        const val START = "Start"
        const val PAUSE = "Pause"
        const val GET_STATUS = "Get_Status"
    }

    private val timerMap = HashMap<Int, Int>()
    private val timers = HashMap<Int, Timer>()
    private val isTimerRunning = HashMap<Int, Boolean>().withDefault { false }

    override fun onBind(p0: Intent?): IBinder? {
        Log.d("Timer", "On Bind Called")
        return null;
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val taskID = intent?.getIntExtra("TaskID", -1)!!

        when (intent.getStringExtra("Action")!!) {
            START -> startTimer(taskID)
            PAUSE -> pauseTimer(taskID)
            GET_STATUS -> sendStatus(taskID)
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startTimer(taskID: Int) {
        if (!timers.containsKey(taskID)) {
            timerMap[taskID] = 0
        }

        isTimerRunning[taskID] = true
        sendStatus(taskID)

        timers[taskID] = Timer()
        timers[taskID]!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val timerIntent = Intent()
                timerIntent.action = "TaskTimer$taskID"

                timerMap[taskID] = timerMap[taskID]!!.plus(1)

//                notificationUpdate(timerMap[taskID]!!, taskID)

                timerIntent.putExtra("TimeElapsed", timerMap[taskID]!!)
                sendBroadcast(timerIntent)
            }
        }, 0, 1000)
    }

    private fun pauseTimer(taskID: Int) {
        timers[taskID]!!.cancel()
        isTimerRunning[taskID] = false
        sendStatus(taskID)
    }

    private fun sendStatus(taskID: Int) {
        val statusIntent = Intent()
        statusIntent.action = "TaskStatus$taskID"
        statusIntent.putExtra("TaskStatus", isTimerRunning[taskID] ?: false)
        statusIntent.putExtra("TaskTimeElapsed", timerMap[taskID] ?: 0)
        sendBroadcast(statusIntent)
    }

//    private fun notificationUpdate(timeElapsed: Int, taskID: Int) {
//        try {
//            val notificationIntent = Intent(this, MainActivity::class.java)
//            val pendingIntent =
//                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT)
//
//            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
//                .setOngoing(true)
//                .setContentTitle("Task Name in progress")
//                .setContentText("Time you have been working on it: $timeElapsed")
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentIntent(pendingIntent)
//                .build()
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                val notificationChannel = NotificationChannel(
//                    CHANNEL_ID,
//                    "Werk Tasks",
//                    NotificationManager.IMPORTANCE_MIN
//                )
//                val notificationManager = getSystemService(NotificationManager::class.java)
//                notificationManager.createNotificationChannel(notificationChannel)
//                notificationManager.notify(taskID, notification)
//            }
//
//            startForeground(taskID, notification)
//        } catch (e: Exception) {
//            Log.e("Timer", e.toString())
//        }
//    }
}