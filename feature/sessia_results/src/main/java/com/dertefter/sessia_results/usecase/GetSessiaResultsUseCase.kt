package com.dertefter.sessia_results.usecase

import com.dertefter.data.dto.sessia_results.SessiaResultDto
import com.dertefter.data.repository.SessiaResultsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSessiaResultsUseCase @Inject constructor(
    private val sessiaResultsRepository: SessiaResultsRepository
) {
    operator fun invoke(): Flow<List<SessiaResultDto>> {
        return sessiaResultsRepository.getSessiaResults()
    }
}
