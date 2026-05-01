package com.dertefter.doc_detail.usecase

import com.dertefter.data.repository.DocsRepository
import javax.inject.Inject

class CheckCancelableUseCase @Inject constructor(
    private val docsRepository: DocsRepository
) {
    suspend operator fun invoke(docId: String): Result<Boolean> {
        return docsRepository.checkCancelable(docId)
    }
}
