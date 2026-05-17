package com.dertefter.messages

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dertefter.design.theme.isFold
import com.dertefter.messages.presentation.Event
import com.dertefter.messages.presentation.MessagesScreenFold
import com.dertefter.messages.presentation.MessagesScreenPhone

@Composable
fun MessagesRoute(
    viewModel: MessagesViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(
            Event.OnUpdateMessages
        )
    }

    if (MaterialTheme.isFold){
        MessagesScreenFold(
            uiState = uiState,
            onEvent = { event ->
                viewModel.onEvent(event)
            },
        )
    } else {
        MessagesScreenPhone(
            uiState = uiState,
            onEvent = { event ->
                viewModel.onEvent(event)
            },
        )
    }




}
