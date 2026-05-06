package com.dertefter.neticlient.screens.schedule

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dertefter.neticlient.screens.schedule.presentation.ScheduleScreen
import com.dertefter.neticlient.screens.schedule.presentation.ScheduleViewModel

@Composable
fun ScheduleRoute(
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ScheduleScreen(
        uiState = state,
        onEvent = viewModel::onEvent
    )
}
