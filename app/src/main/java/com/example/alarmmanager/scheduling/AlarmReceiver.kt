package com.example.alarmmanager.scheduling


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getIntExtra("EXTRA_ALARM_ID", -1)
        val serviceIntent = Intent(context, AlarmService::class.java)

        if (intent.action == "ACTION_DISMISS") {
            context.stopService(serviceIntent)

        } else {
            if (alarmId != -1) {
                serviceIntent.putExtra("EXTRA_ALARM_ID", alarmId)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(serviceIntent)
                } else {
                    context.startService(serviceIntent)
                }
            }
        }
    }
}