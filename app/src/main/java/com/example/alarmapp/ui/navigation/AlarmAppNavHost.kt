package com.example.alarmapp.ui.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.alarmapp.ui.screens.AlarmsScreen
import com.example.alarmapp.ui.screens.TrackerScreen
import com.example.alarmapp.viewmodel.AlarmViewModel

private sealed class Destination(val route: String, val label: String) {
    data object Alarms : Destination("alarms", "Alarms")
    data object Tracker : Destination("tracker", "Tracking")
}

@Composable
fun AlarmAppNavHost(viewModel: AlarmViewModel) {
    val navController = rememberNavController()
    val items = listOf(Destination.Alarms, Destination.Tracker)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { destination ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = { Text(destination.label) },
                        icon = {}
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Alarms.route,
            modifier = androidx.compose.ui.Modifier.padding(padding)
        ) {
            composable(Destination.Alarms.route) {
                AlarmsScreen(viewModel)
            }
            composable(Destination.Tracker.route) {
                TrackerScreen(viewModel)
            }
        }
    }
}
