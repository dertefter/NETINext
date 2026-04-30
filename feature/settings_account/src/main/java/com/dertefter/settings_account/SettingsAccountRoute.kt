package com.dertefter.settings_account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dertefter.settings_account.presentation.SettingsAccountScreen

@Composable
fun SettingsAccountRoute(
    isPanel: Boolean = false,
    viewModel: SettingsAccountViewModel = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    SettingsAccountScreen(
        uiState = uiState,
        isPanel = isPanel,
        onEvent = { event ->
            viewModel.onEvent(event)
        },
    )


}
