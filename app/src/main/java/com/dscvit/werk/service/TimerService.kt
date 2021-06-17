package com.dscvit.werk.service

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.dscvit.werk.R
import com.dscvit.werk.ui.tasks.TaskDescriptionActivity
import java.lang.Exception
import java.util.*

class TimerService : Service() {
    companion object {
        const val CHANNEL_ID = "Werk_Timer_Notifications"
    }

    override fun onBind(p0: Intent?): IBinder? {
        Log.d("Timer", "On Bind Called")
        return null;
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val taskID = intent?.getIntExtra("TaskID", 69)
        var time = 0

        createNotificationChannel()

        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val intentLocal = Intent()
                intentLocal.action = "Counter"

                time++

                notificationUpdate(time)

                Log.d("Timer", "$taskID: $time")
                if (time == 100) {
                    timer.cancel()
                }
                intentLocal.putExtra("TimeElapsed", time)
                sendBroadcast(intentLocal)
            }
        }, 0, 1000)

        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Werk Tasks",
                NotificationManager.IMPORTANCE_MIN
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun notificationUpdate(timeElapsed: Int) {
        try {
            val notificationIntent = Intent(this, TaskDescriptionActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setOngoing(true)
                .setContentTitle("Task Name in progress")
                .setContentText("Time you have been working on it: $timeElapsed")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build()

            startForeground(101, notification)
        } catch (e: Exception) {
            Log.e("Timer", e.toString())
        }
    }
}