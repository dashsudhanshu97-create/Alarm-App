package com.example.alarmapp.data

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AlarmRepository(context: Context) {
    private val prefs = context.getSharedPreferences("alarm_repo", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }

    fun loadAlarms(): List<AlarmItem> {
        val raw = prefs.getString(KEY_ALARMS, null) ?: return emptyList()
        return runCatching { json.decodeFromString<List<AlarmItem>>(raw) }.getOrDefault(emptyList())
    }

    fun saveAlarms(items: List<AlarmItem>) {
        prefs.edit().putString(KEY_ALARMS, json.encodeToString(items)).apply()
    }

    fun loadHabitTracker(): HabitTracker {
        val raw = prefs.getString(KEY_TRACKER, null) ?: return HabitTracker()
        return runCatching { json.decodeFromString<HabitTracker>(raw) }.getOrDefault(HabitTracker())
    }

    fun saveHabitTracker(tracker: HabitTracker) {
        prefs.edit().putString(KEY_TRACKER, json.encodeToString(tracker)).apply()
    }

    companion object {
        private const val KEY_ALARMS = "alarms"
        private const val KEY_TRACKER = "tracker"
    }
}
