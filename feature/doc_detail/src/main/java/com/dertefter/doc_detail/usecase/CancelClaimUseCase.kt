package com.dertefter.doc_detail.usecase

import com.dertefter.data.repository.DocsRepository
import javax.inject.Inject

class CancelClaimUseCase @Inject constructor(
    private val docsRepository: DocsRepository
) {
    suspend operator fun invoke(docId: String): Result<Unit> {
        return docsRepository.cancelClaim(docId)
    }
}
