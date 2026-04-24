package com.dertefter.settings_labs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dertefter.settings_labs.presentation.SettingsLabsScreen

@Composable
fun SettingsLabsRoute(
    isPanel: Boolean = false,
    viewModel: SettingsLabsViewModel = hiltViewModel(),
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
