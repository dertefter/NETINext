package com.dertefter.settings_theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.settings_theme.presentation.Event
import com.dertefter.settings_theme.presentation.UiState
import com.dertefter.settings_theme.usecase.GetIsShapeCutUseCase
import com.dertefter.settings_theme.usecase.GetThemeColorUseCase
import com.dertefter.settings_theme.usecase.NavigateBackUseCase
import com.dertefter.settings_theme.usecase.SaveIsShapeCutUseCase
import com.dertefter.settings_theme.usecase.SaveThemeColorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsThemeViewModel @Inject constructor(
    getThemeColorUseCase: GetThemeColorUseCase,
    getIsShapeCutUseCase: GetIsShapeCutUseCase,
    private val saveThemeColorUseCase: SaveThemeColorUseCase,
    private val saveIsShapeCutUseCase: SaveIsShapeCutUseCase,
    private val navigateBackUseCase: NavigateBackUseCase
) : ViewModel() {

    private val selectedColor = getThemeColorUseCase()

    private val isShapeCut = getIsShapeCutUseCase()

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
                    saveThemeColorUseCase(event.color)
                }
            }

            is Event.OnSetIsShapeCut -> {
                viewModelScope.launch {
                    saveIsShapeCutUseCase(event.isCut)
                }
            }

            is Event.OnNavigateBack -> {
                navigateBackUseCase()
            }

        }
    }


}
