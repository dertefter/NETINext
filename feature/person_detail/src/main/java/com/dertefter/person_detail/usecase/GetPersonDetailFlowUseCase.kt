package com.dertefter.person_detail.usecase

import com.dertefter.data.dto.person.PersonDetailDto
import com.dertefter.data.repository.PersonsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPersonDetailFlowUseCase @Inject constructor(
    private val personsRepository: PersonsRepository
) {
    operator fun invoke(personId: Long): Flow<PersonDetailDto?> {
        return personsRepository.getPersonDetailFlowById(personId)
    }
}
