package com.dertefter.auth

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.dertefter.auth.presentation.AuthScreen

@Composable
fun AuthRoute(
    viewModel: AuthViewModel = hiltViewModel(),
) {

    val uiState = viewModel.state

    AuthScreen(
        uiState = uiState,
        onEvent = { event ->
            viewModel.onEvent(event)
        },
    )

}
