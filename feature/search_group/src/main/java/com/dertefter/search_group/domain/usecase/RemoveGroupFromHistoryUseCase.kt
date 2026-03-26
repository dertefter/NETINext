package com.dertefter.search_group.domain.usecase

import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.repository.GroupsRepository
import javax.inject.Inject

class RemoveGroupFromHistoryUseCase @Inject constructor(

    private val groupsRepository: GroupsRepository
) {
    suspend operator fun invoke(groupDto: GroupDto){
        groupsRepository.removeGroupFromHistory(groupDto)
    }
}