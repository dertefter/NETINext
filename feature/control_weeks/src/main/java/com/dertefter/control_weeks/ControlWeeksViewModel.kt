package com.dertefter.control_weeks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.common.AppError
import com.dertefter.data.common.toAppError
import com.dertefter.control_weeks.presentation.Event
import com.dertefter.control_weeks.presentation.UiState
import com.dertefter.control_weeks.usecase.GetControlWeeksUseCase
import com.dertefter.control_weeks.usecase.NavigateBackUseCase
import com.dertefter.control_weeks.usecase.OpenShareScoreUseCase
import com.dertefter.control_weeks.usecase.UpdateControlWeeksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ControlWeeksViewModel @Inject constructor(
    getControlWeeksUseCase: GetControlWeeksUseCase,
    private val updateControlWeeksUseCase: UpdateControlWeeksUseCase,
    private val navigateBackUseCase: NavigateBackUseCase,
    private val openShareScoreUseCase: OpenShareScoreUseCase
) : ViewModel() {

    private val _isUpdating = MutableStateFlow(false)

    private val _error = MutableStateFlow<AppError?>(null)

    private val _controlWeeks = getControlWeeksUseCase()

    val uiState: StateFlow<UiState> = combine(
        _controlWeeks,
        _isUpdating,
        _error
    ) { controlWeeks, isUpdating, error ->
        UiState(
            controlWeeks = controlWeeks,
            isLoading = isUpdating,
            error = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState()
    )

    init {
        updateSessiaResults()
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnUpdateSessiaResults -> {
                updateSessiaResults()
            }
            is Event.OnNavigateBack -> {
                navigateBackUseCase()
            }

            is Event.OnShare -> {
                openShareScoreUseCase()
            }
        }
    }

    private fun updateSessiaResults() {
        viewModelScope.launch {
            _isUpdating.update { true }
            _error.update { null }
            updateControlWeeksUseCase()
                .onFailure { error ->
                    _error.update { error.toAppError() }
                }
            _isUpdating.update { false }
        }
    }
}
