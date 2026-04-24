package com.dertefter.home

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dertefter.design.theme.isFold
import com.dertefter.home.presentation.Event
import com.dertefter.home.presentation.HomeScreenFold
import com.dertefter.home.presentation.HomeScreenPhone

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
) {

    val uiState by viewModel.newsState.collectAsStateWithLifecycle()

    val scheduleState by viewModel.scheduleState.collectAsStateWithLifecycle()

    val promo by viewModel.promo.collectAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.onEvent(Event.RequestLoadingNews(true))
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(Event.RequestLoadingPromo)
    }


    if (MaterialTheme.isFold){
        HomeScreenFold(
            newsState = uiState,
            scheduleState = scheduleState,
            onEvent = { event ->
                viewModel.onEvent(event)
            },
            promo = promo
        )
    }else{
        HomeScreenPhone(
            newsState = uiState,
            scheduleState = scheduleState,
            onEvent = { event ->
                viewModel.onEvent(event)
            },
            promo = promo
        )
    }



}
