package com.dertefter.data.repository

import com.dertefter.data.dto.person.PersonDetailDto
import com.dertefter.data.dto.person.PersonShortDto
import kotlinx.coroutines.flow.Flow

interface PersonsRepository {


    fun getPersonDetailFlowById(personId: Long): Flow<PersonDetailDto?>

    fun getPersonsFlowByIds(personIds: List<Long>): Flow<List<PersonDetailDto>>

    suspend fun getSearchResults(query: String): Result<List<PersonShortDto>>

    suspend fun updatePersonDetail(personId: Long): Result<PersonDetailDto>


}