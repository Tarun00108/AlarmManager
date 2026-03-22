package com.example.alarmmanager.scheduling

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.alarmmanager.data.AlarmDatabase

class RescheduleAlarmsWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val database = AlarmDatabase.getDatabase(context)
            val repository = com.example.alarmmanager.data.AlarmRepository(database.alarmDao())
            val scheduler = AndroidAlarmScheduler(context)

            val alarms = repository.getEnabledAlarms()
            alarms.forEach { scheduler.schedule(it) }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}