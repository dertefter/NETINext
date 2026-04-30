package com.dertefter.profile.usecase

import com.dertefter.data.dto.auth.AuthStatus
import com.dertefter.data.repository.AuthRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetCiuAuthStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): StateFlow<AuthStatus> {
        return authRepository.ciuAuthStatus
    }
}
