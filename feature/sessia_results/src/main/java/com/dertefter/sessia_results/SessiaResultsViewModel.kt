package com.dertefter.sessia_results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.common.AppError
import com.dertefter.data.common.toAppError
import com.dertefter.data.repository.SessiaResultsRepository
import com.dertefter.navigation.Navigator
import com.dertefter.navigation.Routes
import com.dertefter.sessia_results.presentation.Event
import com.dertefter.sessia_results.presentation.UiState
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
    private val sessiaResultsRepository: SessiaResultsRepository,
    private val navigator: Navigator
) : ViewModel() {

    private val _isUpdating = MutableStateFlow(false)

    private val _error = MutableStateFlow<AppError?>(null)

    private val _sessiaResults = sessiaResultsRepository.getSessiaResults()

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
                navigator.navigateUp()
            }

            is Event.OnShare -> {
                navigator.openAsBottomSheet(Routes.ShareScore)
            }
        }
    }

    private fun updateSessiaResults() {
        viewModelScope.launch {
            _isUpdating.update { true }
            _error.update { null }
            sessiaResultsRepository.updateSessiaResults()
                .onFailure { error ->
                    _error.update { error.toAppError() }
                }
            _isUpdating.update { false }
        }
    }
}
