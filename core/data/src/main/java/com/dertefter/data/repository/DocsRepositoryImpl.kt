package com.dertefter.data.repository

import com.dertefter.data.datasource.local.LocalDataSource
import com.dertefter.data.datasource.remote.RemoteDataSource
import com.dertefter.data.dto.docs.DocsItemDto
import com.dertefter.data.dto.docs.DocumentOptionItem
import com.dertefter.data.dto.docs.DocumentRequestItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DocsRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : DocsRepository {
    override fun getDocsList(): Flow<List<DocsItemDto>> {
        return localDataSource.getDocsList().map { it ?: emptyList() }
    }

    override suspend fun updateDocsList(): Result<List<DocsItemDto>> {
        return remoteDataSource.getDocsList().onSuccess {
            localDataSource.saveDocsList(it)
        }
    }

    override suspend fun fetchOptionsList(): Result<List<DocumentOptionItem>> {
        return remoteDataSource.getOptionsList()
    }

    override suspend fun getDocumentRequestItem(value: String): Result<DocumentRequestItem> {
        return remoteDataSource.getDocumentRequestItem(value)
    }

    override suspend fun claimNewDocument(
        typeClaim: String,
        comment: String
    ): Result<Unit> {
        return remoteDataSource.claimNewDocument(typeClaim, comment)
    }

    override suspend fun checkCancelable(docId: String): Result<Boolean> {
        return remoteDataSource.checkCancelable(docId)
    }

    override suspend fun cancelClaim(docId: String): Result<Unit> {
        return remoteDataSource.cancelDocument(docId)
    }


}
