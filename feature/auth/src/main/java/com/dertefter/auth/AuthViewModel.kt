package com.dertefter.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.auth.presentation.Event
import com.dertefter.auth.presentation.UiState
import com.dertefter.data.common.toAppError
import com.dertefter.data.dto.auth.AuthStatus
import com.dertefter.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _login = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    private val _isPasswordVisible = MutableStateFlow(false)

    private val _isLoading = MutableStateFlow(false)

    private val _isError = MutableStateFlow(false)


    val state: StateFlow<UiState> = combine(
        _login,
        _password,
        _isPasswordVisible,
        _isLoading,
        _isError
    ) { login, password, isPasswordVisible, isLoading, isError ->
        UiState(
            login = login,
            password = password,
            isPasswordVisible = isPasswordVisible,
            isLoading = isLoading,
            isError = isError
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState()
    )

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnLogout -> {
                viewModelScope.launch {
                    authRepository.logout()
                }
            }

            is Event.OnLoginChanged -> {
                _login.value = event.login
            }

            is Event.OnPasswordChanged -> {
                _password.value = event.password
            }

            is Event.OnTogglePasswordVisibility -> {
                _isPasswordVisible.value = !_isPasswordVisible.value
            }

            Event.OnSubmit -> submit()
        }
    }

    private fun submit() {
        viewModelScope.launch {
            val login = _login.value
            val password = _password.value
            _isLoading.value = true
            _isError.value = false
            authRepository.authorizeFull(
                login, password,
                logoutOnFail = true,
                removeAccountOnFail = true,
                updateStatusOnSuccessOnly = true
            ).onSuccess {

            }.onFailure {
                _isError.value = true
            }
            _isLoading.value = false
        }
    }
}
