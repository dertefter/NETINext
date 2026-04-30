package com.dertefter.settings

import androidx.lifecycle.ViewModel
import com.dertefter.settings.usecase.NavigateToRouteUseCase
import com.dertefter.settings.usecase.NavigateUpUseCase
import com.dertefter.settings.presentation.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val navigateToRouteUseCase: NavigateToRouteUseCase,
    private val navigateUpUseCase: NavigateUpUseCase
) : ViewModel() {

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnNavigateTo -> {
                navigateToRouteUseCase(event.route)
            }
            is Event.OnNavigateBack -> {
                navigateUpUseCase()
            }
        }
    }

}
