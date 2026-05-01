package com.dertefter.home.usecase

import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.repository.GroupsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentGroupUseCase @Inject constructor(
    private val groupsRepository: GroupsRepository
) {
    operator fun invoke(): Flow<GroupDto?> {
        return groupsRepository.getCurrentGroup()
    }
}
