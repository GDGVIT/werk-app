package com.dscvit.werk.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.dscvit.werk.R
import com.dscvit.werk.ui.MainActivity
import java.util.*

class TimerService : Service() {
    companion object {
        const val CHANNEL_ID = "Werk_Timer_Notifications"
    }

    private val timerMap = HashMap<Int, Int>()

    override fun onBind(p0: Intent?): IBinder? {
        Log.d("Timer", "On Bind Called")
        return null;
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val taskID = intent?.getIntExtra("TaskID", -1)!!
        timerMap[taskID] = 0

        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val intentLocal = Intent()
                intentLocal.action = "TaskTimer$taskID"

                timerMap[taskID] = timerMap[taskID]!!.plus(1)

                notificationUpdate(timerMap[taskID]!!, taskID)

                Log.d("Timer", "$taskID: ${timerMap[taskID]!!}")
                if (timerMap[taskID] == 100) {
                    timer.cancel()
                }
                intentLocal.putExtra("TimeElapsed", timerMap[taskID]!!)
                sendBroadcast(intentLocal)
            }
        }, 0, 1000)

        return super.onStartCommand(intent, flags, startId)
    }

    private fun notificationUpdate(timeElapsed: Int, taskID: Int) {
        try {
            val notificationIntent = Intent(this, MainActivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT)

            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setOngoing(true)
                .setContentTitle("Task Name in progress")
                .setContentText("Time you have been working on it: $timeElapsed")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(
                    CHANNEL_ID,
                    "Werk Tasks",
                    NotificationManager.IMPORTANCE_MIN
                )
                val notificationManager = getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(notificationChannel)
                notificationManager.notify(taskID, notification)
            }

            startForeground(taskID, notification)
        } catch (e: Exception) {
            Log.e("Timer", e.toString())
        }
    }
}