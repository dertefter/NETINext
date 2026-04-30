package com.dertefter.new_document.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.dto.docs.DocumentOptionItem
import com.dertefter.data.dto.docs.DocumentRequestItem
import com.dertefter.data.repository.DocsRepository
import com.dertefter.navigation.Navigator
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
class NewDocumentViewModel @Inject constructor(
    private val docsRepository: DocsRepository,
    private val navigator: Navigator
) : ViewModel() {

    private val _isUpdating = MutableStateFlow(false)
    private val _isError =  MutableStateFlow(false)

    private val _optionList = MutableStateFlow<List<DocumentOptionItem>>(emptyList())

    private val _documentRequest =  MutableStateFlow<DocumentRequestItem?>(null)

    private val _comment = MutableStateFlow<String>("")

    private val _selectedOption = MutableStateFlow<DocumentOptionItem?>(null)

    val uiState: StateFlow<UiState> = combine(
        _optionList,
        _documentRequest,
        _selectedOption,
        _comment,
        _isUpdating,
        _isError
    ) { args ->
        UiState(
            optionList = args[0] as List<DocumentOptionItem>,
            documentRequest = args[1] as DocumentRequestItem?,
            selectedOption = args[2] as DocumentOptionItem?,
            comment = args[3] as String,
            isLoading = args[4] as Boolean,
            isError = args[5] as Boolean
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState()
    )

    fun onEvent(event: Event) {
        when (event) {

            is Event.OnNavigateUp -> {
                navigator.navigateUp()
            }

            is Event.OnUpdateOptions -> {
                updateOptionsList()
            }

            is Event.OnSelectOption -> {
                selectOption(event.option)
            }

            is Event.OnCommentChanged -> {
                _comment.value = event.s
            }

            is Event.OnConfirmClaim -> {
                val typeClaim = _selectedOption.value?.value
                val comment = _comment.value
                if (typeClaim != null){
                    confirmClaim(typeClaim, comment)
                } else {
                    _isError.update { true }
                }

            }

        }
    }


    private fun confirmClaim(typeClaim: String, comment: String) {
        viewModelScope.launch {
            _isUpdating.update { true }
            _isError.update { false }
            docsRepository.claimNewDocument(typeClaim, comment)
                .onSuccess {
                    navigator.navigateUp()
                }
                .onFailure { error ->
                    _isError.update { true }
                }
            _isUpdating.update { false }
        }
    }

    private fun updateOptionsList() {
        viewModelScope.launch {
            _isUpdating.update { true }
            _isError.update { false }
            docsRepository.fetchOptionsList()
                .onSuccess {
                    _optionList.value = it
                }
                .onFailure { error ->
                    _isError.update { true }
                }
            _isUpdating.update { false }
        }
    }

    private fun selectOption(option: DocumentOptionItem) {
        viewModelScope.launch {
            _isUpdating.update { true }
            _isError.update { false }
            docsRepository.getDocumentRequestItem(option.value)
                .onSuccess {
                    _selectedOption.value = option
                    _documentRequest.value = it
                }
                .onFailure { error ->
                    _selectedOption.value = null
                    _isError.update { true }
                }
            _isUpdating.update { false }
        }
    }

}
