package com.dertefter.data.repository

import com.dertefter.data.datasource.local.LocalDataSource
import com.dertefter.data.datasource.remote.RemoteDataSource
import com.dertefter.data.dto.sessia_results.SessiaResultDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SessiaResultsRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : SessiaResultsRepository {

    override fun getSessiaResults(): Flow<List<SessiaResultDto>> {
        return localDataSource.getSessiaResults().map { it ?: emptyList() }
    }

    override suspend fun updateSessiaResults(): Result<List<SessiaResultDto>> {
        return remoteDataSource.getSessiaResults().onSuccess {
            localDataSource.saveSessiaResults(sessiaResults = it)
        }
    }


}