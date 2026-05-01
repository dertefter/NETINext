package com.dertefter.settings_about

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dertefter.settings_about.presentation.SettingsAboutScreen

@Composable
fun SettingsAboutRoute(
    isPanel: Boolean = false,
    viewModel: SettingsAboutViewModel = hiltViewModel(),
) {

    SettingsAboutScreen(
        isPanel = isPanel,
        onEvent = { event ->
            viewModel.onEvent(event)
        },
    )



}
