package com.dertefter.settings_notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.settings_notifications.usecase.GetNotificationStatusUseCase
import com.dertefter.settings_notifications.usecase.NavigateUpUseCase
import com.dertefter.settings_notifications.usecase.SaveNotificationEnabledUseCase
import com.dertefter.settings_notifications.presentation.Event
import com.dertefter.settings_notifications.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsNotificationsViewModel @Inject constructor(
    getNotificationStatusUseCase: GetNotificationStatusUseCase,
    private val saveNotificationEnabledUseCase: SaveNotificationEnabledUseCase,
    private val navigateUpUseCase: NavigateUpUseCase
    ) : ViewModel() {

    private val isNotificationEnabled = getNotificationStatusUseCase()
        
    val uiState: StateFlow<UiState> = isNotificationEnabled.map {
        UiState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState(false)
    )

    fun onEvent(event: Event) {
        when (event) {

            is Event.OnChangeNotificationStatus -> {
                viewModelScope.launch {
                    saveNotificationEnabledUseCase(event.isEnabled)
                }
            }

            is Event.OnNavigateBack -> {
                navigateUpUseCase()
            }

        }
    }


}
