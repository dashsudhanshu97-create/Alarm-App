package com.example.alarmapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.alarmapp.data.AlarmRepository
import com.example.alarmapp.scheduler.AlarmScheduler

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        val repository = AlarmRepository(context)
        val scheduler = AlarmScheduler(context)
        repository.loadAlarms().filter { it.enabled }.forEach { scheduler.schedule(it) }
    }
}
