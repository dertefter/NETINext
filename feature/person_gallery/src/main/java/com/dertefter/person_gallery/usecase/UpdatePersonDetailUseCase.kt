package com.dertefter.person_gallery.usecase

import com.dertefter.data.repository.PersonsRepository
import javax.inject.Inject

class UpdatePersonDetailUseCase @Inject constructor(
    private val personsRepository: PersonsRepository
) {
    suspend operator fun invoke(personId: Long) {
        personsRepository.updatePersonDetail(personId)
    }
}
