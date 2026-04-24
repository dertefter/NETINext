package com.dertefter.settings_notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.repository.SettingsRepository
import com.dertefter.navigation.Navigator
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
    private val settingsRepository: SettingsRepository,
    private val navigator: Navigator
    ) : ViewModel() {

    private val isNotificationEnabled = settingsRepository.isNotificationEnabled
        
    val uiState: StateFlow<UiState> = isNotificationEnabled.map {
        UiState(it == true)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState(false)
    )

    fun onEvent(event: Event) {
        when (event) {

            is Event.OnChangeNotificationStatus -> {
                viewModelScope.launch {
                    settingsRepository.saveNotificationEnabled(event.isEnabled)
                }
            }

            is Event.OnNavigateBack -> {
                navigator.navigateUp()
            }

        }
    }


}
