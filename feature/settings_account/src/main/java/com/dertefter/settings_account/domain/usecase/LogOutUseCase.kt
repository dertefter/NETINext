package com.dertefter.settings_account.domain.usecase

import com.dertefter.data.repository.AuthRepository
import com.dertefter.navigation.Navigator
import javax.inject.Inject

class LogOutUseCase @Inject constructor(

    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(){
        return authRepository.logout()
    }
}