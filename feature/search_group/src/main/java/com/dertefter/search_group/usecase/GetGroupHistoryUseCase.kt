package com.dertefter.search_group.usecase

import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.repository.GroupsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGroupHistoryUseCase @Inject constructor(

    private val groupsRepository: GroupsRepository
) {
    operator fun invoke(): Flow<List<GroupDto>> {
        return groupsRepository.getGroupHistory()
    }
}