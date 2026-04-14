package com.example.alarmapp.data

import kotlinx.serialization.Serializable
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

@Serializable
enum class RepeatType { NONE, DAILY, WEEKLY }

@Serializable
data class AlarmItem(
    val id: String = UUID.randomUUID().toString(),
    val label: String,
    val startAt: String,
    val intervalMinutes: Int,
    val occurrences: Int,
    val repeatType: RepeatType,
    val weeklyDays: Set<Int> = emptySet(),
    val enabled: Boolean = true,
) {
    fun parsedStartAt(): LocalDateTime = LocalDateTime.parse(startAt)
}

@Serializable
data class HabitTracker(
    val sleepTime: String = LocalTime.of(23, 0).toString(),
    val wakeTime: String = LocalTime.of(7, 0).toString(),
    val foodTimes: List<String> = listOf("08:00", "13:00", "20:00"),
    val meditationTime: String = "06:30",
    val waterIntakeMl: Int = 2000,
    val waterProgressMl: Int = 0,
)

fun DayOfWeek.toIntId(): Int = value
fun Int.toDayOfWeek(): DayOfWeek = DayOfWeek.of(this)
