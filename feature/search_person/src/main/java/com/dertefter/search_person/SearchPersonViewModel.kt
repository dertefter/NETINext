package com.dertefter.search_person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.common.toAppError
import com.dertefter.search_person.presentation.Event
import com.dertefter.search_person.presentation.UiState
import com.dertefter.search_person.usecase.GetPersonSearchResultsUseCase
import com.dertefter.search_person.usecase.NavigateBackUseCase
import com.dertefter.search_person.usecase.NavigateToPersonDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPersonViewModel @Inject constructor(
    private val getPersonSearchResultsUseCase: GetPersonSearchResultsUseCase,
    private val navigateToPersonDetailUseCase: NavigateToPersonDetailUseCase,
    private val navigateBackUseCase: NavigateBackUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState("", emptyList()))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnSearchQueryChanged -> {
                searchPersons(event.query)
            }

            is Event.OnOpenPerson -> {
                navigateToPersonDetailUseCase(event.personId)
            }
            is Event.OnNavigateBack -> {
                navigateBackUseCase()
            }
        }
    }

    private var searchJob: Job? = null

    private fun searchPersons(q: String) {
        _uiState.update { it.copy(query = q) }
        if (q.isBlank()) {
            _uiState.update { it.copy(persons = emptyList(), isLoading = false, error = null) }
            searchJob?.cancel()
            return
        }
        _uiState.update { it.copy(isLoading = true, error = null) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            getPersonSearchResultsUseCase(q).onSuccess { persons ->
                _uiState.update { it.copy(persons = persons, isLoading = false) }
            }.onFailure { throwable ->
                _uiState.update { it.copy(isLoading = false, error = throwable.toAppError()) }
            }
        }
    }

}
