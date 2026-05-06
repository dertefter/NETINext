package com.dertefter.search_group.usecase

import com.dertefter.data.repository.GroupsRepository
import javax.inject.Inject

class ClearGroupHistoryUseCase @Inject constructor(

    private val groupsRepository: GroupsRepository
) {
    suspend operator fun invoke(){
        groupsRepository.clearGroupHistory()
    }
}