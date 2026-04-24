package com.dertefter.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.dertefter.data.datasource.local.LocalDataSource
import com.dertefter.data.datasource.local.room.entity.toDomain
import com.dertefter.data.datasource.remote.NewsRemoteMediator
import com.dertefter.data.datasource.remote.RemoteDataSource
import com.dertefter.data.dto.news.NewsDetailDto
import com.dertefter.data.dto.news.NewsItem
import com.dertefter.data.dto.news.PromoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : NewsRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getNews(): Flow<PagingData<NewsItem>> {
        val pageSize = 20
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                prefetchDistance = pageSize,
                initialLoadSize = pageSize,
                enablePlaceholders = true
            ),
            remoteMediator = NewsRemoteMediator(localDataSource, remoteDataSource, pageSize),
            pagingSourceFactory = { localDataSource.getNewsPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun getNewsForPage(page: Int): Result<List<NewsItem>> {
        return remoteDataSource.getNewsForPage(page)
    }

    override suspend fun getPromo(): Result<List<PromoItem>> {
        return remoteDataSource.getPromo().onSuccess {
            localDataSource.savePromo(it)
        }
    }

    override fun getCachedPromo(): Flow<List<PromoItem>?> {
        return localDataSource.getPromo()
    }

    override suspend fun getNewsDetail(newsId: String): Result<NewsDetailDto> {
        return remoteDataSource.getNewsDetail(newsId)
    }


}
