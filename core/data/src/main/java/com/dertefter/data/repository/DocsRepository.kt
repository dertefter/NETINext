package com.dertefter.data.repository

import com.dertefter.data.dto.docs.DocsItemDto
import com.dertefter.data.dto.docs.DocumentOptionItem
import com.dertefter.data.dto.docs.DocumentRequestItem
import kotlinx.coroutines.flow.Flow

interface DocsRepository {

    fun getDocsList(): Flow<List<DocsItemDto>>

    suspend fun updateDocsList(): Result<List<DocsItemDto>>

    suspend fun fetchOptionsList(): Result<List<DocumentOptionItem>>

    suspend fun getDocumentRequestItem(value: String): Result<DocumentRequestItem>

    suspend fun claimNewDocument(typeClaim: String, comment: String): Result<Unit>

    suspend fun checkCancelable(docId: String): Result<Boolean>

    suspend fun cancelClaim(docId: String): Result<Unit>

}
