package com.dertefter.docs.usecase

import com.dertefter.data.dto.docs.DocsItemDto
import com.dertefter.data.repository.DocsRepository
import javax.inject.Inject

class UpdateDocsListUseCase @Inject constructor(
    private val docsRepository: DocsRepository
) {
    suspend operator fun invoke(): Result<List<DocsItemDto>> {
        return docsRepository.updateDocsList()
    }
}
