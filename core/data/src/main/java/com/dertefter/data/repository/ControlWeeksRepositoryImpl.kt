package com.dertefter.data.repository

import com.dertefter.data.datasource.local.LocalDataSource
import com.dertefter.data.datasource.remote.RemoteDataSource
import com.dertefter.data.dto.control_weeks.ControlWeekDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ControlWeeksRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : ControlWeeksRepository {

    override fun getControlWeeks(): Flow<List<ControlWeekDto>> {
        return localDataSource.getControlWeeks().map { it ?: emptyList() }
    }

    override suspend fun updateControlWeeks(): Result<List<ControlWeekDto>> {
        return remoteDataSource.getControlWeeks().onSuccess {
            localDataSource.saveControlWeeks(sessiaResults = it)
        }
    }


}