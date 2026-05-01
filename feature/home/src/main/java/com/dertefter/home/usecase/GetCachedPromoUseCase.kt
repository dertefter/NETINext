package com.dertefter.home.usecase

import com.dertefter.data.dto.news.PromoItem
import com.dertefter.data.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCachedPromoUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(): Flow<List<PromoItem>?> {
        return newsRepository.getCachedPromo()
    }
}
