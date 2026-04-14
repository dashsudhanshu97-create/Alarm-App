package com.example.alarmapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.alarmapp.viewmodel.AlarmViewModel

@Composable
fun AlarmsScreen(viewModel: AlarmViewModel) {
    var label by remember { mutableStateOf("Morning Routine") }
    var delayMinutes by remember { mutableIntStateOf(30) }
    var intervalMinutes by remember { mutableIntStateOf(30) }
    var count by remember { mutableIntStateOf(4) }
    var dailyRepeat by remember { mutableStateOf(true) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        item {
            Text("Smart Alarm Stack", style = MaterialTheme.typography.headlineSmall)
            Text("Set multiple alarms at fixed gaps (e.g., 30, 60, 90, 120 mins)")
        }
        item {
            OutlinedTextField(
                value = label,
                onValueChange = { label = it },
                label = { Text("Alarm label") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = delayMinutes.toString(),
                onValueChange = { delayMinutes = it.toIntOrNull() ?: 30 },
                label = { Text("First alarm after (minutes)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = intervalMinutes.toString(),
                onValueChange = { intervalMinutes = it.toIntOrNull() ?: 30 },
                label = { Text("Gap between alarms (minutes)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            OutlinedTextField(
                value = count.toString(),
                onValueChange = { count = (it.toIntOrNull() ?: 4).coerceIn(1, 10) },
                label = { Text("How many alarms") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Repeat daily")
                Switch(checked = dailyRepeat, onCheckedChange = { dailyRepeat = it })
            }
        }
        item {
            Button(
                onClick = {
                    val alarm = viewModel.createStackedAlarm(
                        label = label,
                        afterMinutes = delayMinutes,
                        intervalMinutes = intervalMinutes,
                        occurrences = count,
                        repeatDaily = dailyRepeat,
                        repeatDays = emptySet()
                    )
                    viewModel.addAlarm(alarm)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create alarm stack")
            }
        }

        item {
            Text("Visible alarms", fontWeight = FontWeight.Bold)
        }

        items(viewModel.alarms, key = { it.id }) { alarm ->
            Card {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(alarm.label, style = MaterialTheme.typography.titleMedium)
                    Text("Start: ${alarm.startAt}")
                    Text("Every ${alarm.intervalMinutes} min • ${alarm.occurrences} rings • ${alarm.repeatType}")
                    Text("Next alarms:")
                    viewModel.upcomingPreview(alarm).take(4).forEach {
                        Text("• $it")
                    }
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Row {
                            Text("Enabled")
                            Switch(
                                checked = alarm.enabled,
                                onCheckedChange = { viewModel.toggleAlarm(alarm.id, it) }
                            )
                        }
                        Button(onClick = { viewModel.removeAlarm(alarm.id) }) { Text("Delete") }
                    }
                }
            }
        }
    }
}
