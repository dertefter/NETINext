package com.dertefter.data.repository

import com.dertefter.data.dto.control_weeks.ControlWeekDto
import kotlinx.coroutines.flow.Flow

interface ControlWeeksRepository {

    fun getControlWeeks(): Flow<List<ControlWeekDto>>

    suspend fun updateControlWeeks(): Result<List<ControlWeekDto>>

}