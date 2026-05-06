package com.dertefter.data.repository

import com.dertefter.data.dto.schedule.GroupDto
import kotlinx.coroutines.flow.Flow

interface GroupsRepository {

    suspend fun getSearchResults(query: String): Result<List<GroupDto>>

    fun getGroupHistory(): Flow<List<GroupDto>>

    suspend fun removeGroupFromHistory(group: GroupDto)

    suspend fun clearGroupHistory()

    fun getCurrentGroup(): Flow<GroupDto?>

    suspend fun setCurrentGroup(group: GroupDto)

}