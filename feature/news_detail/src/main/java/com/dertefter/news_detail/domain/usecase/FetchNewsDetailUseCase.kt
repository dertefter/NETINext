package com.dertefter.news_detail.domain.usecase

import com.dertefter.data.dto.news.NewsDetailDto
import com.dertefter.data.repository.NewsRepository
import javax.inject.Inject

class FetchNewsDetailUseCase @Inject constructor(

    private val newsRepository: NewsRepository,
) {
    suspend operator fun invoke(newsId: String): Result<NewsDetailDto> {
        return newsRepository.getNewsDetail(newsId)
    }
}