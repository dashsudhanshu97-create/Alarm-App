# Alarm-App (Kotlin Android)

A Kotlin Android alarm planner app built with Jetpack Compose.

## Included features

- **Stacked multi-alarms**: create chained alarms like `30m`, `60m`, `90m`, `120m`.
- **Repeat controls**:
  - One-time alarms
  - Daily repeat
  - Weekly repeat (data model + scheduler support)
- **Visible alarm timeline**:
  - Current alarm list
  - Next upcoming ring times preview
- **Habit tracking**:
  - Sleep time & wake time
  - Food times
  - Meditation reminder time
  - Water intake target + current progress
- **Alarm reliability basics**:
  - Uses `AlarmManager.setExactAndAllowWhileIdle`
  - Re-schedules after reboot via `BOOT_COMPLETED`
  - High-priority alarm notifications

## Tech stack

- Kotlin
- Jetpack Compose (Material 3)
- Android AlarmManager + BroadcastReceiver
- SharedPreferences + Kotlinx Serialization for persistence

## Project structure

- `app/src/main/java/com/example/alarmapp/ui`: Compose UI screens and navigation
- `app/src/main/java/com/example/alarmapp/viewmodel`: UI state and actions
- `app/src/main/java/com/example/alarmapp/scheduler`: Alarm schedule logic
- `app/src/main/java/com/example/alarmapp/receiver`: Alarm/boot receivers
- `app/src/main/java/com/example/alarmapp/data`: Models + local repository

## Build/run

1. Open in Android Studio (Hedgehog+ recommended).
2. Let Gradle sync.
3. Run on Android device (API 26+).
4. Grant notification and exact alarm permissions when prompted by the OS.

## Notes

This is a strong starter implementation focused on your requested scheduling and tracking workflows. Advanced anti-dismiss wake-up missions, cloud backup/account sync, and full ringtone/challenge suite can be added next as phase-2 features.
