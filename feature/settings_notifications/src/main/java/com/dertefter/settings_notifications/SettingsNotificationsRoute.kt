package com.dertefter.settings_notifications

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dertefter.settings_notifications.presentation.SettingsLabsScreen

@Composable
fun SettingsNotificationsRoute(
    isPanel: Boolean = false,
    viewModel: SettingsNotificationsViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsLabsScreen(
        uiState = uiState,
        isPanel = isPanel,
        onEvent = { event ->
            viewModel.onEvent(event)
        },
    )



}
