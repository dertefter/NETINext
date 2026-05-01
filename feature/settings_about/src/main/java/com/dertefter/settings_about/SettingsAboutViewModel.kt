package com.dertefter.settings_about

import androidx.lifecycle.ViewModel
import com.dertefter.settings_about.presentation.Event
import com.dertefter.settings_about.usecase.NavigateBackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsAboutViewModel @Inject constructor(
    private val navigateBackUseCase: NavigateBackUseCase
) : ViewModel() {


    fun onEvent(event: Event) {
        when (event) {
            is Event.OnNavigateBack -> {
                navigateBackUseCase()
            }

        }
    }


}
