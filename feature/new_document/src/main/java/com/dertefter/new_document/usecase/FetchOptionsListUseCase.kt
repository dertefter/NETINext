package com.dertefter.new_document.usecase

import com.dertefter.data.dto.docs.DocumentOptionItem
import com.dertefter.data.repository.DocsRepository
import javax.inject.Inject

class FetchOptionsListUseCase @Inject constructor(
    private val docsRepository: DocsRepository
) {
    suspend operator fun invoke(): Result<List<DocumentOptionItem>> {
        return docsRepository.fetchOptionsList()
    }
}
