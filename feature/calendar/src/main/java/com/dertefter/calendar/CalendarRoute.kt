package com.dertefter.calendar

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dertefter.calendar.presentation.CalendarScreenFold
import com.dertefter.calendar.presentation.CalendarScreenPhone
import com.dertefter.design.theme.isFold

@Composable
fun CalendarRoute(
    viewModel: CalendarViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (MaterialTheme.isFold){
        CalendarScreenFold(
            uiState = uiState,
            onEvent = { event ->
                viewModel.onEvent(event)
            },
        )
    }else{
        CalendarScreenPhone(
            uiState = uiState,
            onEvent = { event ->
                viewModel.onEvent(event)
            },
        )
    }


}
