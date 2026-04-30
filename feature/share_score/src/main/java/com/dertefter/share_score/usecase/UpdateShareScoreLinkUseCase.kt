package com.dertefter.share_score.usecase

import com.dertefter.data.repository.ShareScoreRepository
import javax.inject.Inject

class UpdateShareScoreLinkUseCase @Inject constructor(
    private val shareScoreRepository: ShareScoreRepository
) {
    suspend operator fun invoke(generateNew: Boolean = false): Result<String> {
        return shareScoreRepository.updateShareScoreLink(generateNew)
    }
}
