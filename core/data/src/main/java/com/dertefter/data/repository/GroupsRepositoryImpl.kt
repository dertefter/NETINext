package com.dertefter.data.repository

import com.dertefter.data.datasource.local.LocalDataSource
import com.dertefter.data.datasource.remote.RemoteDataSource
import com.dertefter.data.dto.schedule.GroupDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GroupsRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : GroupsRepository {


    override suspend fun getSearchResults(query: String): Result<List<GroupDto>> {
        val result = remoteDataSource.getSearchGroupResults(query)
        return result
    }

    override fun getGroupHistory(): Flow<List<GroupDto>> {
        return localDataSource.getGroupHistory()
    }

    override suspend fun removeGroupFromHistory(group: GroupDto) {
        val currentHistory = localDataSource.getGroupHistory().first()
        val newHistory = currentHistory.filter { it.name != group.name }
        localDataSource.saveGroupHistory(newHistory)
    }

    private suspend fun addGroupToHistory(group: GroupDto) {
        val currentHistory = localDataSource.getGroupHistory().first()
        val newHistory = listOf(group) + currentHistory.filter { it.name != group.name }
        localDataSource.saveGroupHistory(newHistory.take(10))
    }

    override fun getCurrentGroup(): Flow<GroupDto?> {
        return localDataSource.getCurrentGroup()
    }

    override suspend fun setCurrentGroup(group: GroupDto) {
        addGroupToHistory(group)
        localDataSource.saveCurrentGroup(group)
    }

}
