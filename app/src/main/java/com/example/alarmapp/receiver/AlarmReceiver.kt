package com.example.alarmapp.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "alarm_channel"

        if (manager.getNotificationChannel(channelId) == null) {
            manager.createNotificationChannel(
                NotificationChannel(channelId, "Alarm Alerts", NotificationManager.IMPORTANCE_HIGH)
            )
        }

        val label = intent.getStringExtra("alarmLabel") ?: "Alarm"
        val number = intent.getIntExtra("alarmNumber", 1)
        val total = intent.getIntExtra("totalAlarms", 1)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("$label • Ring $number/$total")
            .setContentText("Your scheduled alarm is ringing now.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        manager.notify((label + number).hashCode(), notification)
    }
}
