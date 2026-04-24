package com.dertefter.settings_theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.repository.SettingsRepository
import com.dertefter.navigation.Navigator
import com.dertefter.settings_theme.presentation.Event
import com.dertefter.settings_theme.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsThemeViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val navigator: Navigator
    ) : ViewModel() {

    private val selectedColor = settingsRepository.themeColor

    private val isShapeCut = settingsRepository.isShapeCut
    
    val uiState: StateFlow<UiState> = combine(
        selectedColor,
        isShapeCut
    ) { color, isCut ->
        UiState(color, isCut)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState()
    )

    fun onEvent(event: Event) {
        when (event) {

            is Event.OnSelectColor -> {
                viewModelScope.launch {
                    settingsRepository.saveThemeColor(event.color)
                }
            }

            is Event.OnSetIsShapeCut -> {
                viewModelScope.launch {
                    settingsRepository.saveIsShapeCut(event.isCut)
                }
            }

            is Event.OnNavigateBack -> {
                navigator.navigateUp()
            }

        }
    }


}
