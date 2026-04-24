package com.dertefter.data.datasource.local.room.entity

import androidx.room.Entity
import com.dertefter.data.dto.schedule.TimeSlotDto

@Entity(tableName = "schedules", primaryKeys = ["login", "groupName"])
data class ScheduleEntity(
    val login: String,
    val groupName: String,
    val timeSlots: List<TimeSlotDto>
)
