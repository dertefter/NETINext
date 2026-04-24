package com.dertefter.messages_detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dertefter.messages_detail.presentation.MessagesDetailScreen

@Composable
fun MessagesDetailRoute(
    idMessage: Long,
    idStudent: Long?,
    isPanel: Boolean = false,
    viewModel: MessagesDetailViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(idMessage, idStudent, isPanel) {
        viewModel.initWith(
            idMessage,
            idStudent,
            isPanel
        )
    }

    MessagesDetailScreen(
        uiState = uiState,
        onEvent = { event ->
            viewModel.onEvent(event)
        },
        isPanel = isPanel,
    )


}
