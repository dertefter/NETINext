package com.dertefter.person_gallery.usecase

import com.dertefter.data.repository.PersonsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAvatarUrlFlowUseCase @Inject constructor(
    private val personsRepository: PersonsRepository
) {
    operator fun invoke(personId: Long): Flow<String> {
        return personsRepository.getPersonDetailFlowById(personId).map { it?.avatarUrl ?: "" }
    }
}
