package com.dertefter.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dertefter.auth.presentation.AuthStatus
import com.dertefter.auth.presentation.Event
import com.dertefter.auth.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    var state by mutableStateOf(UiState(authStatus = AuthStatus.Unauthorized))
        private set

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnLoginChanged -> {
                state = state.copy(login = event.login)
            }
            is Event.OnPasswordChanged -> {
                state = state.copy(password = event.password)
            }
            Event.OnSubmit -> {
                // TODO: Implement login logic
            }
        }
    }
}
