package com.dertefter.new_document

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dertefter.new_document.presentation.Event
import com.dertefter.new_document.presentation.NewDocumentScreen
import com.dertefter.new_document.presentation.NewDocumentViewModel

@Composable
fun NewDocumentRoute(
    viewModel: NewDocumentViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(Event.OnUpdateOptions)
    }

    NewDocumentScreen(
        uiState = uiState,
        onEvent = { event ->
            viewModel.onEvent(event)
        },
    )


}
