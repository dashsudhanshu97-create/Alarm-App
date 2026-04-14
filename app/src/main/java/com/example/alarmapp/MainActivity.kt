package com.example.alarmapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.alarmapp.ui.navigation.AlarmAppNavHost
import com.example.alarmapp.viewmodel.AlarmViewModel

class MainActivity : ComponentActivity() {
    private val viewModel by lazy { AlarmViewModel(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    AlarmAppNavHost(viewModel = viewModel)
                }
            }
        }
    }
}
