package com.dertefter.profile.usecase

import com.dertefter.data.dto.user.UserInfoDto
import com.dertefter.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<UserInfoDto?> {
        return userRepository.getUserInfo()
    }
}
