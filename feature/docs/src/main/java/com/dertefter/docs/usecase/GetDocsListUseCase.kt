package com.dertefter.docs.usecase

import com.dertefter.data.dto.docs.DocsItemDto
import com.dertefter.data.repository.DocsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDocsListUseCase @Inject constructor(
    private val docsRepository: DocsRepository
) {
    operator fun invoke(): Flow<List<DocsItemDto>> {
        return docsRepository.getDocsList()
    }
}
