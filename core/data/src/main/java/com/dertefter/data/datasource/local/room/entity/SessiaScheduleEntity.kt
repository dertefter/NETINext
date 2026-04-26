package com.dertefter.data.datasource.local.room.entity

import androidx.room.Entity
import com.dertefter.data.dto.schedule.TimeSlotDto

@Entity(tableName = "sessia_schedules", primaryKeys = ["login", "groupName"])
data class SessiaScheduleEntity(
    val login: String,
    val groupName: String,
    val timeSlots: List<TimeSlotDto>
)
