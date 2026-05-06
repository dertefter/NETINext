package com.dertefter.profile

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dertefter.design.theme.isFold
import com.dertefter.profile.presentation.ProfileScreenFold
import com.dertefter.profile.presentation.ProfileScreenPhone

@Composable
fun ProfileRoute(
    viewModel: ProfileViewModel = hiltViewModel(),
) {

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    if (MaterialTheme.isFold){
        ProfileScreenFold(
            uiState = uiState,
            onEvent = { event ->
                viewModel.onEvent(event)
            },
        )
    }else{
        ProfileScreenPhone(
            uiState = uiState,
            onEvent = { event ->
                viewModel.onEvent(event)
            },
        )
    }
}
