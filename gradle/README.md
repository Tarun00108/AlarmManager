# AlarmManager App ⏰

A robust and modern Android application built with Kotlin and XML that allows users to schedule exact, persistent alarms. The app features a highly reliable "Background-First" architecture, ensuring alarms trigger exactly on time with sound and notifications, even if the app is killed from memory or the device is rebooted.

## 🛠 Tech Stack & Architecture

*   **Language:** Kotlin
*   **UI:** XML with ViewBinding (Material3 Components)
*   **Architecture:** MVVM + Repository Pattern
*   **Scheduling:** `AlarmManager` (Exact timing) & `WorkManager` (Guaranteed execution)
*   **Dependency Injection:** Dagger Hilt
*   **Local Database:** Room Database (SQLite)
*   **Background Tasks:** `ForegroundService` & `BroadcastReceiver`
*   **Async:** Kotlin Coroutines & LiveData

## ✅ Features Implemented

1.  **Exact Scheduling:** Users can create, view, and toggle multiple precise alarms using a `TimePickerDialog`.
2.  **Reboot Persistence:** Implemented a `BootCompletedReceiver` and `WorkManager` to automatically read from the Room DB and re-schedule all active alarms after a device restart.
3.  **Background Execution:** Uses a `ForegroundService` with a `specialUse` type to play media and show notifications, preventing the Android OS from killing the alarm process.
4.  **Actionable Notifications:** Implemented full-screen intent notifications that wake the device screen and provide actionable buttons (e.g., Dismiss).
5.  **Modern Permission Handling:** Graceful, runtime permission requests tailored for Android 12 (`SCHEDULE_EXACT_ALARM`), Android 13 (`POST_NOTIFICATIONS`), and Android 14 (`FOREGROUND_SERVICE_SPECIAL_USE`).
6.  **Empty State UI:** Provides clean visual feedback (Material icons and text) when the user has no alarms scheduled.

## 🧐 Assumptions Made

*   **Battery Optimization:** It is assumed the user will grant the requested "Exact Alarms" permission. On some heavily customized Android skins (like Xiaomi or Huawei), users might still need to manually remove battery restrictions for 100% background reliability.
*   **System Sound:** The app assumes the device has a valid default alarm ringtone configured (`Settings.System.DEFAULT_ALARM_ALERT_URI`).
*   **Time Handling:** For simplicity, the `TimePickerDialog` defaults to a 24-hour format. If a scheduled time has already passed for the current day, the app automatically assumes the alarm is for the *next* day.

## ⚠️ Known Limitations

1.  **Custom Ringtones:** The app currently plays the system's default alarm sound. Selecting custom audio files from the device storage is not implemented in this version.
2.  **Repeating Alarms:** The current scheduling logic handles single-occurrence exact alarms. Complex recurring schedules (e.g., "Every Monday and Wednesday") would require a more complex database schema and scheduling logic.
3.  **Snooze Logic:** While the Notification UI can support a Snooze action, the current primary implementation focuses on dismissing and cancelling the active alarm cycle.

## 🚀 How to Run

1.  Clone the repository to your local machine.
2.  Open the project in Android Studio (Giraffe / 2022.3.1 or newer recommended).
3.  Sync Gradle dependencies (ensure Dagger Hilt generates the necessary boilerplate).
4.  Build and Run on an Emulator or Physical Device.
    *   *Note: To fully test background execution and reboot persistence, testing on a physical device is highly recommended.*