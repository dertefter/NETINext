package com.dertefter.settings_theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dertefter.settings_theme.presentation.SettingsThemeScreen

@Composable
fun SettingsThemeRoute(
    isPanel: Boolean = false,
    viewModel: SettingsThemeViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsThemeScreen(
        uiState = uiState,
        isPanel = isPanel,
        onEvent = { event ->
            viewModel.onEvent(event)
        },
    )



}
