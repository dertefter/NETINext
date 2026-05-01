package com.dertefter.lesson_detail.usecase

import com.dertefter.data.dto.person.PersonDetailDto
import com.dertefter.data.repository.PersonsRepository
import javax.inject.Inject

class UpdatePersonDetailUseCase @Inject constructor(
    private val personsRepository: PersonsRepository
) {
    suspend operator fun invoke(personId: Long): Result<PersonDetailDto> {
        return personsRepository.updatePersonDetail(personId)
    }
}
