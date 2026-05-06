package com.dertefter.neticlient.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dertefter.neticlient.screens.home.presentation.HomeEvent
import com.dertefter.neticlient.screens.home.presentation.HomeScreen
import com.dertefter.neticlient.screens.home.presentation.HomeViewModel

@Composable
fun HomeRoute(
    onNavigateToSchedule: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val scheduleState by viewModel.scheduleState.collectAsStateWithLifecycle()

    HomeScreen(
        scheduleState = scheduleState,
        onEvent = { event ->
            if (event == HomeEvent.OnOpenDetails) {
                onNavigateToSchedule()
            } else {
                viewModel.onEvent(event)
            }
        }
    )
}
