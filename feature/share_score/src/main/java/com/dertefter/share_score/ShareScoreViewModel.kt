package com.dertefter.share_score

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.common.AppError
import com.dertefter.data.common.toAppError
import com.dertefter.share_score.presentation.Event
import com.dertefter.share_score.presentation.UiState
import com.dertefter.share_score.usecase.GetShareScoreLinkUseCase
import com.dertefter.share_score.usecase.UpdateShareScoreLinkUseCase
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
class ShareScoreViewModel @Inject constructor(
    getShareScoreLinkUseCase: GetShareScoreLinkUseCase,
    private val updateShareScoreLinkUseCase: UpdateShareScoreLinkUseCase
) : ViewModel() {

    private val _isUpdating = MutableStateFlow(false)

    private val _error = MutableStateFlow<AppError?>(null)

    private val _shareScoreLink = getShareScoreLinkUseCase()

    val uiState: StateFlow<UiState> = combine(
        _shareScoreLink,
        _isUpdating,
        _error
    ) { shareScoreLink, isUpdating, error ->
        UiState(
            shareScoreLink = shareScoreLink,
            isLoading = isUpdating,
            error = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState()
    )

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnUpdateLink -> {
                updateLink(false)
            }
            is Event.OnRegenerateLink -> {
                updateLink(true)
            }
        }
    }

    private fun updateLink(generateNew: Boolean) {
        viewModelScope.launch {
            _isUpdating.update { true }
            _error.update { null }
            updateShareScoreLinkUseCase(generateNew)
                .onFailure { error ->
                    Log.e("updateLink", error.stackTraceToString())
                    _error.update {
                        error.toAppError()
                    }
                }
            _isUpdating.update { false }
        }
    }
}
