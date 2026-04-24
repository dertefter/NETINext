package com.dertefter.data.repository

import com.dertefter.data.dto.sessia_results.SessiaResultDto
import kotlinx.coroutines.flow.Flow

interface SessiaResultsRepository {

    fun getSessiaResults(): Flow<List<SessiaResultDto>>

    suspend fun updateSessiaResults(): Result<List<SessiaResultDto>>

}