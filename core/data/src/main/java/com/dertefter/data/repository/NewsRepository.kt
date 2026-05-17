package com.dertefter.data.repository

import androidx.paging.PagingData
import com.dertefter.data.dto.news.NewsDetailDto
import com.dertefter.data.dto.news.NewsItem
import com.dertefter.data.dto.news.PromoItem
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getNews(): Flow<PagingData<NewsItem>>

    suspend fun getNewsForPage(page: Int): Result<List<NewsItem>>

    suspend fun getPromo(): Result<List<PromoItem>>

    fun getCachedPromo(): Flow<List<PromoItem>?>

    suspend fun getNewsDetail(newsId: String): Result<NewsDetailDto>

}