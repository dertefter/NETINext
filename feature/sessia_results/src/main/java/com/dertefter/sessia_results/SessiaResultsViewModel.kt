package com.dertefter.sessia_results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.common.AppError
import com.dertefter.data.common.toAppError
import com.dertefter.sessia_results.presentation.Event
import com.dertefter.sessia_results.presentation.UiState
import com.dertefter.sessia_results.usecase.GetSessiaResultsUseCase
import com.dertefter.sessia_results.usecase.NavigateBackUseCase
import com.dertefter.sessia_results.usecase.OpenShareScoreUseCase
import com.dertefter.sessia_results.usecase.UpdateSessiaResultsUseCase
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
class SessiaResultsViewModel @Inject constructor(
    getSessiaResultsUseCase: GetSessiaResultsUseCase,
    private val updateSessiaResultsUseCase: UpdateSessiaResultsUseCase,
    private val navigateBackUseCase: NavigateBackUseCase,
    private val openShareScoreUseCase: OpenShareScoreUseCase
) : ViewModel() {

    private val _isUpdating = MutableStateFlow(false)

    private val _error = MutableStateFlow<AppError?>(null)

    private val _sessiaResults = getSessiaResultsUseCase()

    val uiState: StateFlow<UiState> = combine(
        _sessiaResults,
        _isUpdating,
        _error
    ) { sessiaResults, isUpdating, error ->
        UiState(
            sessiaResults = sessiaResults,
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
            updateSessiaResultsUseCase()
                .onFailure { error ->
                    _error.update { error.toAppError() }
                }
            _isUpdating.update { false }
        }
    }
}
