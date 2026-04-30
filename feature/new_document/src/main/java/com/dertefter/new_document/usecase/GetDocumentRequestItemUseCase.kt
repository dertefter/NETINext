package com.dertefter.new_document.usecase

import com.dertefter.data.dto.docs.DocumentRequestItem
import com.dertefter.data.repository.DocsRepository
import javax.inject.Inject

class GetDocumentRequestItemUseCase @Inject constructor(
    private val docsRepository: DocsRepository
) {
    suspend operator fun invoke(value: String): Result<DocumentRequestItem> {
        return docsRepository.getDocumentRequestItem(value)
    }
}
