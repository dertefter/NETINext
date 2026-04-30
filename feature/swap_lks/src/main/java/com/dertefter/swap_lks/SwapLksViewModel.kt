package com.dertefter.swap_lks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.common.AppError
import com.dertefter.data.common.toAppError
import com.dertefter.swap_lks.presentation.Event
import com.dertefter.swap_lks.presentation.UiState
import com.dertefter.swap_lks.usecase.GetLksListUseCase
import com.dertefter.swap_lks.usecase.HideBottomShitUseCase
import com.dertefter.swap_lks.usecase.SetSelectedLksUseCase
import com.dertefter.swap_lks.usecase.UpdateLksListUseCase
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
class SwapLksViewModel @Inject constructor(
    getLksListUseCase: GetLksListUseCase,
    private val updateLksListUseCase: UpdateLksListUseCase,
    private val setSelectedLksUseCase: SetSelectedLksUseCase,
    private val hideBottomShitUseCase: HideBottomShitUseCase,
) : ViewModel() {

    private val _isUpdating = MutableStateFlow(false)

    private val _swapLksId = MutableStateFlow<Int?>(null)

    private val _error = MutableStateFlow<AppError?>(null)

    private val _lksList = getLksListUseCase()

    val uiState: StateFlow<UiState> = combine(
        _lksList,
        _isUpdating,
        _swapLksId,
        _error
    ) { lksList, isUpdating, swapLksId, error ->
        UiState(
            lksList = lksList,
            isLoading = isUpdating,
            settingLksId = swapLksId,
            error = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState()
    )

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnUpdateLks -> {
                updateLksList()
            }
            is Event.OnSetLks -> {
                setLksById(event.lksId)
            }
        }
    }

    private fun updateLksList() {
        viewModelScope.launch {
            _isUpdating.update { true }
            _error.update { null }
            updateLksListUseCase()
                .onFailure { error ->
                    _error.update {
                        error.toAppError()
                    }
                }
            _isUpdating.update { false }
        }
    }

    private fun setLksById(lksId: Int) {
        if (_swapLksId.value != null) return
        viewModelScope.launch {
            _swapLksId.update { lksId }
            _error.update { null }
            setSelectedLksUseCase(lksId)
                .onSuccess {
                    updateLksListUseCase()
                    hideBottomShitUseCase()
                }
            _swapLksId.update { null }
        }
    }
}
