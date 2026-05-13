package com.dertefter.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dertefter.calendar.presentation.ScheduleScreen
import com.dertefter.calendar.presentation.ScheduleViewModel

@Composable
fun ScheduleRoute(
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ScheduleScreen(
        uiState = state
    )
}
