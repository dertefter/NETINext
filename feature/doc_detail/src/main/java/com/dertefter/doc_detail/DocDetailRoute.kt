package com.dertefter.doc_detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dertefter.doc_detail.presentation.DocDetailScreen
import com.dertefter.doc_detail.presentation.Event

@Composable
fun DocDetailRoute(
    type: String,
    date: String?,
    status: String?,
    person: String?,
    comment: String?,
    number: String?,
    viewModel: DocDetailViewModel = hiltViewModel(),
) {

    LaunchedEffect(number) {
        number?.let{ number ->
            viewModel.onEvent(
                Event.OnCheckCancelable(number)
            )
        }

    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DocDetailScreen(
        type = type,
        date = date,
        status = status,
        person = person,
        comment = comment,
        number = number,
        uiState = uiState,
        onEvent = { event ->
            viewModel.onEvent(event)
        },
    )

}
