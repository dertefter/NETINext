package com.dertefter.share_score.usecase

import com.dertefter.data.repository.ShareScoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetShareScoreLinkUseCase @Inject constructor(
    private val shareScoreRepository: ShareScoreRepository
) {
    operator fun invoke(): Flow<String?> = shareScoreRepository.getShareScoreLink()
}
