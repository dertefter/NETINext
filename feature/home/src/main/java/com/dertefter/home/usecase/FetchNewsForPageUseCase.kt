package com.dertefter.home.usecase

import com.dertefter.data.dto.news.NewsItem
import com.dertefter.data.repository.NewsRepository
import javax.inject.Inject

class FetchNewsForPageUseCase @Inject constructor(

    private val newsRepository: NewsRepository,
) {
    suspend operator fun invoke(page: Int): Result<List<NewsItem>> {
        return newsRepository.getNewsForPage(page)
    }
}