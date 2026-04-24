package com.dertefter.person_detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dertefter.person_detail.presentation.PersonDetailScreen

@Composable
fun PersonDetailRoute(
    personId: Long,
    viewModel: PersonDetailViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(personId) {
        viewModel.initWith(personId)
    }

    PersonDetailScreen(
        uiState = uiState,
        onEvent = { event ->
            viewModel.onEvent(event)
        },
    )


}
