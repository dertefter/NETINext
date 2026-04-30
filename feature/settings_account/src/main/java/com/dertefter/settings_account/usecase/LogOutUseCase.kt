package com.dertefter.settings_account.usecase

import com.dertefter.data.repository.AuthRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(

    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(){
        return authRepository.logout()
    }
}