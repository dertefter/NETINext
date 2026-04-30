package com.dertefter.search_person.usecase

import com.dertefter.data.dto.person.PersonShortDto
import com.dertefter.data.repository.PersonsRepository
import javax.inject.Inject

class GetPersonSearchResultsUseCase @Inject constructor(
    private val personsRepository: PersonsRepository
) {
    suspend operator fun invoke(query: String): Result<List<PersonShortDto>> {
        return personsRepository.getSearchResults(query)
    }
}
