package com.dertefter.docs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.docs.presentation.Event
import com.dertefter.docs.presentation.UiState
import com.dertefter.docs.usecase.GetDocsListUseCase
import com.dertefter.docs.usecase.NavigateBackUseCase
import com.dertefter.docs.usecase.NavigateToNewDocumentUseCase
import com.dertefter.docs.usecase.OpenDocDetailUseCase
import com.dertefter.docs.usecase.UpdateDocsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DocsViewModel @Inject constructor(
    getDocsListUseCase: GetDocsListUseCase,
    private val updateDocsListUseCase: UpdateDocsListUseCase,
    private val openDocDetailUseCase: OpenDocDetailUseCase,
    private val navigateBackUseCase: NavigateBackUseCase,
    private val navigateToNewDocumentUseCase: NavigateToNewDocumentUseCase
) : ViewModel() {

    private val _isUpdating = MutableStateFlow(false)
    private val _isError =  MutableStateFlow(false)

    private val _docs = getDocsListUseCase()


    val uiState: StateFlow<UiState> = combine(
        _docs,
        _isUpdating,
        _isError
    ) { docs, isUpdating, isError ->
        UiState(
            docsList = docs,
            isLoading = isUpdating,
            isError = isError
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState()
    )

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnUpdate -> {
                updateDocsList()
            }
            is Event.OnOpenDocDetail -> {
                openDocDetailUseCase(event.docItem)
            }
            is Event.OnNavigateBack -> {
                navigateBackUseCase()
            }
            is Event.OnNavigateToNewDocument -> {
                navigateToNewDocumentUseCase()
            }
        }
    }

    private fun updateDocsList() {
        viewModelScope.launch {
            _isUpdating.update { true }
            _isError.update { false }
            updateDocsListUseCase()
                .onFailure { _ ->
                    _isError.update { true }
                }
            _isUpdating.update { false }
        }
    }
}
