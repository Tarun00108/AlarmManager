package com.example.alarmmanager.scheduling

import com.example.alarmmanager.data.Alarm

interface AlarmScheduler {
    fun schedule(alarm: Alarm)
    fun cancel(alarmId: Int)
}