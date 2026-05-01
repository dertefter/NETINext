package com.dertefter.lesson_detail.usecase

import com.dertefter.data.dto.person.PersonDetailDto
import com.dertefter.data.repository.PersonsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPersonsByIdsUseCase @Inject constructor(
    private val personsRepository: PersonsRepository
) {
    operator fun invoke(ids: List<Long>): Flow<List<PersonDetailDto>> {
        return personsRepository.getPersonsFlowByIds(ids)
    }
}
