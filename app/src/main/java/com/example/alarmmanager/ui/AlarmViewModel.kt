package com.example.alarmmanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.example.alarmmanager.data.Alarm
import com.example.alarmmanager.data.AlarmRepository
import com.example.alarmmanager.scheduling.AlarmScheduler
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val repository: AlarmRepository,
    private val scheduler: AlarmScheduler
) : ViewModel() {

    val alarms = repository.allAlarms

    fun scheduleAlarm(hour: Int, minute: Int) {
        viewModelScope.launch {
            val newAlarm = Alarm(hour = hour, minute = minute)
            val alarmId = repository.insert(newAlarm).toInt()
            val createdAlarm = newAlarm.copy(id = alarmId)
            scheduler.schedule(createdAlarm)
        }
    }

    fun updateAlarm(alarm: Alarm) {
        viewModelScope.launch {
            repository.update(alarm)
            if (alarm.isEnabled) {
                scheduler.schedule(alarm)
            } else {
                scheduler.cancel(alarm.id)
            }
        }
    }

    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch {
            scheduler.cancel(alarm.id)
            repository.delete(alarm.id)
        }
    }
}