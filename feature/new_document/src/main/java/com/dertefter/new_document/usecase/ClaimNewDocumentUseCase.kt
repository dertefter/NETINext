package com.dertefter.new_document.usecase

import com.dertefter.data.repository.DocsRepository
import javax.inject.Inject

class ClaimNewDocumentUseCase @Inject constructor(
    private val docsRepository: DocsRepository
) {
    suspend operator fun invoke(typeClaim: String, comment: String): Result<Unit> {
        return docsRepository.claimNewDocument(typeClaim, comment)
    }
}
