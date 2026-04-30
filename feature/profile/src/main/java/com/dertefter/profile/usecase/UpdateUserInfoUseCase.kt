package com.dertefter.profile.usecase

import com.dertefter.data.dto.user.UserInfoDto
import com.dertefter.data.repository.UserRepository
import javax.inject.Inject

class UpdateUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<UserInfoDto?> {
        return userRepository.updateUserInfo()
    }
}
