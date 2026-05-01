package com.dertefter.home.usecase

import androidx.paging.PagingData
import com.dertefter.data.dto.news.NewsItem
import com.dertefter.data.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(): Flow<PagingData<NewsItem>> {
        return newsRepository.getNews()
    }
}
