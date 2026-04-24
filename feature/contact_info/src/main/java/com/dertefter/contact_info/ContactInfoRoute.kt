package com.dertefter.contact_info

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dertefter.contact_info.presentation.ContactInfoScreen

@Composable
fun ContactInfoRoute(
    viewModel: ContactInfoViewModel = hiltViewModel(),
) {

    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val savingState by viewModel.savingState.collectAsStateWithLifecycle()

    ContactInfoScreen(
        uiState = uiState,
        savingState = savingState,
        onEvent = { event ->
            viewModel.onEvent(event)
        },
    )


}
