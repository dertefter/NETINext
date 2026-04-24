package com.dertefter.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dertefter.design.theme.isFold
import com.dertefter.settings.presentation.SettingsScreenFold
import com.dertefter.settings.presentation.SettingsScreenPhone

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel = hiltViewModel(),
) {

    if (MaterialTheme.isFold){
        SettingsScreenFold(
            onEvent = { event ->
                viewModel.onEvent(event)
            },
        )
    } else {
        SettingsScreenPhone(
            onEvent = { event ->
                viewModel.onEvent(event)
            },
        )
    }




}
