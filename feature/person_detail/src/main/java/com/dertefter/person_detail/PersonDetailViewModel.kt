package com.dertefter.person_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.common.toAppError
import com.dertefter.person_detail.presentation.Event
import com.dertefter.person_detail.presentation.UiState
import com.dertefter.person_detail.usecase.GetPersonDetailFlowUseCase
import com.dertefter.person_detail.usecase.NavigateBackUseCase
import com.dertefter.person_detail.usecase.OpenAvatarUseCase
import com.dertefter.person_detail.usecase.UpdatePersonDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonDetailViewModel @Inject constructor(
    private val getPersonDetailFlowUseCase: GetPersonDetailFlowUseCase,
    private val updatePersonDetailUseCase: UpdatePersonDetailUseCase,
    private val openAvatarUseCase: OpenAvatarUseCase,
    private val navigateBackUseCase: NavigateBackUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _personId = MutableStateFlow<Long?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val personDetailFlow = _personId.filterNotNull().flatMapLatest { id ->
        getPersonDetailFlowUseCase(id)
    }

    init {
        viewModelScope.launch {
            personDetailFlow.collect { personDetail ->
                _uiState.update { it.copy(personDetail = personDetail) }
            }
        }
    }

    fun initWith(personId: Long) {
        _personId.value = personId
        updatePersonDetail(personId)
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnUpdatePersonDetail -> {
                _personId.value?.let {
                    updatePersonDetail(it)
                }
            }
            is Event.OnOpenAvatar -> {
                openAvatarUseCase(event.url)
            }
            is Event.OnNavigateBack -> {
                navigateBackUseCase()
            }
        }
    }

    private fun updatePersonDetail(personId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            updatePersonDetailUseCase(personId).onSuccess { personDetailDto ->
                _uiState.update { it.copy(personDetail = personDetailDto, isLoading = false) }
            }.onFailure { error ->
                Log.e("updatePersonDetail", error.stackTraceToString())
                _uiState.update { it.copy(error = error.toAppError(), isLoading = false) }
            }
        }
    }
}
