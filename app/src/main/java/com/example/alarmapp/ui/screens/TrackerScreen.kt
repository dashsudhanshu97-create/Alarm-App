package com.example.alarmapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.alarmapp.data.HabitTracker
import com.example.alarmapp.viewmodel.AlarmViewModel

@Composable
fun TrackerScreen(viewModel: AlarmViewModel) {
    val tracker = viewModel.tracker
    var sleep by remember(tracker.sleepTime) { mutableStateOf(tracker.sleepTime) }
    var wake by remember(tracker.wakeTime) { mutableStateOf(tracker.wakeTime) }
    var food by remember(tracker.foodTimes) { mutableStateOf(tracker.foodTimes.joinToString(", ")) }
    var meditation by remember(tracker.meditationTime) { mutableStateOf(tracker.meditationTime) }
    var waterTarget by remember(tracker.waterIntakeMl) { mutableIntStateOf(tracker.waterIntakeMl) }
    var waterCurrent by remember(tracker.waterProgressMl) { mutableIntStateOf(tracker.waterProgressMl) }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Sleep, Food & Wellness Tracker", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(sleep, { sleep = it }, label = { Text("Sleep time (HH:mm)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(wake, { wake = it }, label = { Text("Wake time (HH:mm)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(food, { food = it }, label = { Text("Food times (comma separated HH:mm)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(meditation, { meditation = it }, label = { Text("Meditation time (HH:mm)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(waterTarget.toString(), { waterTarget = it.toIntOrNull() ?: waterTarget }, label = { Text("Daily water target (ml)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(waterCurrent.toString(), { waterCurrent = it.toIntOrNull() ?: waterCurrent }, label = { Text("Water consumed (ml)") }, modifier = Modifier.fillMaxWidth())

        Text("Hydration: $waterCurrent / $waterTarget ml")

        Button(
            onClick = {
                viewModel.updateTracker(
                    HabitTracker(
                        sleepTime = sleep,
                        wakeTime = wake,
                        foodTimes = food.split(',').map { it.trim() }.filter { it.isNotBlank() },
                        meditationTime = meditation,
                        waterIntakeMl = waterTarget,
                        waterProgressMl = waterCurrent,
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save tracking plan")
        }
    }
}
