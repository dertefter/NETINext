package com.dertefter.swap_lks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dertefter.swap_lks.presentation.Event
import com.dertefter.swap_lks.presentation.SwapLksScreen

@Composable
fun SwapLksRoute(
    viewModel: SwapLksViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(Event.OnUpdateLks)
    }

    SwapLksScreen(
        uiState = uiState,
        onEvent = { event ->
            viewModel.onEvent(event)
        },
    )


}
