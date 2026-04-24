package com.dertefter.data.repository

import com.dertefter.data.datasource.local.LocalDataSource
import com.dertefter.data.datasource.remote.RemoteDataSource
import com.dertefter.data.dto.person.PersonDetailDto
import com.dertefter.data.dto.person.PersonShortDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PersonsRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : PersonsRepository {

    override fun getPersonDetailFlowById(personId: Long): Flow<PersonDetailDto?> {
        return localDataSource.getPersonDetailById(personId)
    }

    override fun getPersonsFlowByIds(personIds: List<Long>): Flow<List<PersonDetailDto>> {
        return localDataSource.getPersonDetailsByIds(personIds)
    }

    override suspend fun getSearchResults(query: String): Result<List<PersonShortDto>> {
        return remoteDataSource.getSearchPersonResults(query)
    }

    override suspend fun updatePersonDetail(personId: Long): Result<PersonDetailDto> {
        return remoteDataSource.getPersonById(personId).onSuccess {
            localDataSource.savePersonDetail(it)
        }
    }


}