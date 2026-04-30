package com.dertefter.swap_lks.usecase

import com.dertefter.data.repository.UserRepository
import javax.inject.Inject

class SetSelectedLksUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(lksId: Int): Result<Unit> {
        return userRepository.setSelectedLks(lksId)
    }
}
