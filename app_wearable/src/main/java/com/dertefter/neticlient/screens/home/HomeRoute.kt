package com.dertefter.neticlient.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dertefter.neticlient.screens.home.presentation.HomeScreen
import com.dertefter.neticlient.screens.home.presentation.HomeViewModel

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val scheduleState by viewModel.scheduleState.collectAsStateWithLifecycle()

    HomeScreen(
        scheduleState = scheduleState,
        onEvent = viewModel::onEvent
    )
}
