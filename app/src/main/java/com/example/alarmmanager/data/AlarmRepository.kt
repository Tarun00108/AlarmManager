package com.example.alarmmanager.data

import javax.inject.Inject

class AlarmRepository @Inject constructor(private val alarmDao: AlarmDao) {
    val allAlarms = alarmDao.getAllAlarms()

    suspend fun insert(alarm: Alarm) = alarmDao.insert(alarm)
    suspend fun update(alarm: Alarm) = alarmDao.update(alarm)
    suspend fun delete(id: Int) = alarmDao.delete(id)
    suspend fun getEnabledAlarms() = alarmDao.getEnabledAlarms()
}