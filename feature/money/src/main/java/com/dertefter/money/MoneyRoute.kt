package com.dertefter.money

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dertefter.money.presentation.Event
import com.dertefter.money.presentation.MoneyScreen

@Composable
fun MoneyRoute(
    viewModel: MoneyViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(Event.OnUpdateYears)
    }

    MoneyScreen(
        uiState = uiState,
        onEvent = { event ->
            viewModel.onEvent(event)
        },
    )


}
