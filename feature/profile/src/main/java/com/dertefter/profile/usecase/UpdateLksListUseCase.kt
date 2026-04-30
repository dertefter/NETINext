package com.dertefter.profile.usecase

import com.dertefter.data.dto.user.LksDto
import com.dertefter.data.repository.UserRepository
import javax.inject.Inject

class UpdateLksListUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<List<LksDto>?> {
        return userRepository.updateLksList()
    }
}
