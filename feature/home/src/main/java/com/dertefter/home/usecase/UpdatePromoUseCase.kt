package com.dertefter.home.usecase

import com.dertefter.data.repository.NewsRepository
import javax.inject.Inject

class UpdatePromoUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke() {
        newsRepository.getPromo()
    }
}
