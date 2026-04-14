package com.example.alarmapp.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.alarmapp.data.AlarmItem
import com.example.alarmapp.data.RepeatType
import com.example.alarmapp.receiver.AlarmReceiver
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZoneId

class AlarmScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun schedule(item: AlarmItem) {
        if (!item.enabled) return

        nextTriggers(item).forEachIndexed { idx, triggerAt ->
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("alarmLabel", item.label)
                putExtra("alarmId", item.id)
                putExtra("alarmNumber", idx + 1)
                putExtra("totalAlarms", item.occurrences)
            }
            val requestCode = (item.id + idx).hashCode()
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                pendingIntent,
            )
        }
    }

    fun cancel(item: AlarmItem) {
        repeat(item.occurrences) { idx ->
            val intent = Intent(context, AlarmReceiver::class.java)
            val requestCode = (item.id + idx).hashCode()
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            alarmManager.cancel(pendingIntent)
        }
    }

    fun nextTriggers(item: AlarmItem): List<LocalDateTime> {
        val start = item.parsedStartAt().let {
            if (it.isBefore(LocalDateTime.now())) it.plusDays(1) else it
        }

        val rawTriggers = (0 until item.occurrences).map { idx ->
            start.plusMinutes((idx * item.intervalMinutes).toLong())
        }

        return when (item.repeatType) {
            RepeatType.NONE -> rawTriggers
            RepeatType.DAILY -> rawTriggers
            RepeatType.WEEKLY -> {
                if (item.weeklyDays.isEmpty()) rawTriggers
                else rawTriggers.map { nextWeekly(it, item.weeklyDays.map { d -> DayOfWeek.of(d) }.toSet()) }
            }
        }
    }

    private fun nextWeekly(target: LocalDateTime, allowedDays: Set<DayOfWeek>): LocalDateTime {
        var candidate = target
        repeat(14) {
            if (allowedDays.contains(candidate.dayOfWeek)) return candidate
            candidate = candidate.plusDays(1)
        }
        return target
    }
}
