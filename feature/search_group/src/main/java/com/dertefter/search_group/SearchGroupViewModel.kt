package com.dertefter.search_group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.common.toAppError
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.search_group.domain.usecase.GetGroupHistoryUseCase
import com.dertefter.search_group.domain.usecase.GetSearchResultsUseCase
import com.dertefter.search_group.domain.usecase.NavigateBackUseCase
import com.dertefter.search_group.domain.usecase.RemoveGroupFromHistoryUseCase
import com.dertefter.search_group.domain.usecase.SelectGroupUseCase
import com.dertefter.search_group.presentation.Event
import com.dertefter.search_group.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchGroupViewModel @Inject constructor(
    private val getGroupHistoryUseCase: GetGroupHistoryUseCase,
    private val selectGroupUseCase: SelectGroupUseCase,
    private val navigateBackUseCase: NavigateBackUseCase,
    private val removeGroupFromHistoryUseCase: RemoveGroupFromHistoryUseCase,
    private val getSearchResultsUseCase: GetSearchResultsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState("", emptyList()))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getGroupHistoryUseCase().collect { history ->
                _uiState.update { it.copy(groupHistory = history) }
            }
        }
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnSearchQueryChanged -> {
                getSearchResults(query = event.query)
            }

            is Event.OnSelectGroup -> {
                selectGroup(event.groupDto)
            }

            is Event.OnRemoveGroupFromHistory -> {
                removeGroupFromHistory(event.groupDto)
            }
        }
    }

    private fun getSearchResults(query: String) {
        _uiState.update { it.copy(query = query) }
        _uiState.update { it.copy(error = null) }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getSearchResultsUseCase(query).onSuccess { groups ->
                    _uiState.update { it.copy(groups = groups) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.toAppError()) }
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun removeGroupFromHistory(groupDto: GroupDto){
        viewModelScope.launch {
            removeGroupFromHistoryUseCase(groupDto)
        }
    }

    private fun selectGroup(groupDto: GroupDto) {
        viewModelScope.launch {
            selectGroupUseCase(groupDto)
            navigateBackUseCase()
        }
    }

}
