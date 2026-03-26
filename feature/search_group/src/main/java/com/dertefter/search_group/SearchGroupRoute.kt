package com.dertefter.search_group

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dertefter.search_group.presentation.Event
import com.dertefter.search_group.presentation.SearchGroupScreen

@Composable
fun SearchGroupRoute(
    viewModel: SearchGroupViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(Event.OnSearchQueryChanged(""))
    }

    SearchGroupScreen(
        uiState = uiState,
        onEvent = { event ->
            viewModel.onEvent(event)
        },
    )

}
