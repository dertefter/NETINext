package com.dertefter.doc_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.repository.DocsRepository
import com.dertefter.doc_detail.presentation.Event
import com.dertefter.doc_detail.presentation.UiState
import com.dertefter.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DocDetailViewModel @Inject constructor(
    private val docsRepository: DocsRepository,
    private val navigator: Navigator,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _cancelable = MutableStateFlow(false)

    val uiState: StateFlow<UiState> = combine(
        _isLoading,
        _cancelable
    ) { isLoading, cancelable ->
        UiState(
            isLoading = isLoading,
            cancelable = cancelable
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState()
    )

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnCheckCancelable -> {
                checkCancelable(event.docNumber)
            }

            is Event.OnCancelClaim -> {
                cancelClaim(event.docNumber)
            }

            is Event.OnNavigateUp -> {
                navigator.hideBottomSheet()
            }

        }
    }


    private fun checkCancelable(docNumber: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _cancelable.value = false
            docsRepository.checkCancelable(docNumber)
                .onSuccess {
                    Log.e("zzzzzzzzzzzzzz", it.toString())
                    _cancelable.value = it
                }
                .onFailure {
                    Log.e("zzzzzzzzzzzzzz", it.stackTraceToString())
                    _cancelable.value = false
                }
            _isLoading.value = false
        }
    }

    private fun cancelClaim(docNumber: String) {
        viewModelScope.launch {
            _isLoading.value = true
            docsRepository.cancelClaim(docNumber)
                .onSuccess {
                    navigator.hideBottomSheet()
                }
                .onFailure {
                    Log.e("ssss", it.stackTraceToString())
                }
            _isLoading.value = false
        }
    }

}
