package com.dertefter.profile.usecase

import com.dertefter.data.dto.user.LksDto
import com.dertefter.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLksListUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<List<LksDto>?> {
        return userRepository.getLksList()
    }
}
