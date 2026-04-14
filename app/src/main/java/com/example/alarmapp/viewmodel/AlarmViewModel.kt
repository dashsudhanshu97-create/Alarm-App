package com.example.alarmapp.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.alarmapp.data.AlarmItem
import com.example.alarmapp.data.AlarmRepository
import com.example.alarmapp.data.HabitTracker
import com.example.alarmapp.scheduler.AlarmScheduler
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AlarmViewModel(context: Context) : ViewModel() {
    private val repository = AlarmRepository(context)
    private val scheduler = AlarmScheduler(context)

    var alarms by mutableStateOf(repository.loadAlarms())
        private set

    var tracker by mutableStateOf(repository.loadHabitTracker())
        private set

    fun addAlarm(item: AlarmItem) {
        alarms = alarms + item
        repository.saveAlarms(alarms)
        scheduler.schedule(item)
    }

    fun toggleAlarm(id: String, enabled: Boolean) {
        val updated = alarms.map {
            if (it.id == id) it.copy(enabled = enabled) else it
        }
        val changed = updated.firstOrNull { it.id == id } ?: return
        alarms = updated
        repository.saveAlarms(alarms)
        if (enabled) scheduler.schedule(changed) else scheduler.cancel(changed)
    }

    fun removeAlarm(id: String) {
        val item = alarms.firstOrNull { it.id == id } ?: return
        scheduler.cancel(item)
        alarms = alarms.filterNot { it.id == id }
        repository.saveAlarms(alarms)
    }

    fun updateTracker(updated: HabitTracker) {
        tracker = updated
        repository.saveHabitTracker(updated)
    }

    fun upcomingPreview(item: AlarmItem): List<String> {
        val formatter = DateTimeFormatter.ofPattern("EEE, MMM d • hh:mm a")
        return scheduler.nextTriggers(item).map { it.format(formatter) }
    }

    fun createStackedAlarm(
        label: String,
        afterMinutes: Int,
        intervalMinutes: Int,
        occurrences: Int,
        repeatDaily: Boolean,
        repeatDays: Set<Int>,
    ): AlarmItem {
        val start = LocalDateTime.of(LocalDate.now(), LocalTime.now()).plusMinutes(afterMinutes.toLong())
        return AlarmItem(
            label = label,
            startAt = start.withSecond(0).withNano(0).toString(),
            intervalMinutes = intervalMinutes,
            occurrences = occurrences,
            repeatType = when {
                repeatDays.isNotEmpty() -> com.example.alarmapp.data.RepeatType.WEEKLY
                repeatDaily -> com.example.alarmapp.data.RepeatType.DAILY
                else -> com.example.alarmapp.data.RepeatType.NONE
            },
            weeklyDays = repeatDays,
        )
    }
}
