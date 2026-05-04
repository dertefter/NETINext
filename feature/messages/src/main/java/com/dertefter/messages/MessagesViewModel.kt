package com.dertefter.messages

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.common.AppError
import com.dertefter.data.common.toAppError
import com.dertefter.data.dto.messsages.MessageDto
import com.dertefter.data.repository.SettingsRepository
import com.dertefter.messages.presentation.Event
import com.dertefter.messages.presentation.FilterMode
import com.dertefter.messages.presentation.UiState
import com.dertefter.messages.usecase.DeleteMessageForeverUseCase
import com.dertefter.messages.usecase.GetMessagesFlowUseCase
import com.dertefter.messages.usecase.MoveMessageToTrashUseCase
import com.dertefter.messages.usecase.NavigateToMessageDetailUseCase
import com.dertefter.messages.usecase.RemoveMessageFromTrashUseCase
import com.dertefter.messages.usecase.UpdateMessagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    getMessagesFlowUseCase: GetMessagesFlowUseCase,
    private val updateMessagesUseCase: UpdateMessagesUseCase,
    private val moveMessageToTrashUseCase: MoveMessageToTrashUseCase,
    private val removeMessageFromTrashUseCase: RemoveMessageFromTrashUseCase,
    private val deleteMessageForeverUseCase: DeleteMessageForeverUseCase,
    private val navigateToMessageDetailUseCase: NavigateToMessageDetailUseCase,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _isUpdating = MutableStateFlow(false)
    private val _error = MutableStateFlow<AppError?>(null)
    private val _messages: Flow<List<MessageDto>> = getMessagesFlowUseCase()

    private val _filterMode = MutableStateFlow<FilterMode>(FilterMode.ALL)

    private val _isAlertSkipped = settingsRepository.isMessagesAlertSkipped

    val uiState: StateFlow<UiState> = combine(
        _messages,
        _isUpdating,
        _error,
        _filterMode,
        _isAlertSkipped
    ) { messages, isUpdating, error, filterMode, isAlertSkipped ->
        val filteredMessages = when (filterMode) {
            FilterMode.ALL -> messages.filter { it.isDeleted == 0 }
            FilterMode.UNREAD -> messages.filter { it.isRead == 0 && it.isDeleted == 0 }
            FilterMode.DELETED -> messages.filter { it.isDeleted == 1 }
            is FilterMode.TAB -> messages.filter { it.senderType == filterMode.tabId && it.isDeleted == 0 }
        }
        UiState(
            messages = filteredMessages.sortedByDescending { it.getLocalDateTime() },
            isLoading = isUpdating,
            error = error,
            filterModes = listOf(FilterMode.ALL, FilterMode.UNREAD)
                    + messages.map { FilterMode.TAB(it.senderType) }.distinct(),
            filterMode = filterMode,
            isAlertSkipped = isAlertSkipped == true
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState()
    )

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnUpdateMessages -> {
                updateMessages()
            }

            is Event.OnSkipAlert -> {
                skipAlert()
            }

            is Event.OnUpdateFilterMode -> {
                _filterMode.value = event.filterMode
            }

            is Event.OnMoveToArchive -> {
                archiveMessage(event.idStudent, event.idMessage)
            }

            is Event.OnRemoveFromArchive -> {
                restoreMessage(event.idStudent, event.idMessage)
            }

            is Event.OnDeleteForever -> {
                deleteForever(event.idStudent, event.idMessage)
            }

            is Event.OnOpenMessageDetail -> {
                navigateToMessageDetailUseCase(event.idStudent, event.idMessage)
            }

        }
    }

    private fun updateMessages() {
        viewModelScope.launch {
            _isUpdating.value = true
            _error.value = null
            updateMessagesUseCase().onFailure {
                _error.value = it.toAppError()
            }
            _isUpdating.value = false
        }
    }

    private fun skipAlert() {
        viewModelScope.launch {
            settingsRepository.saveIsMessagesAlertSkipped(true)
        }
    }

    private fun archiveMessage(idStudent:  Long?, idMessage:  Long) {
        viewModelScope.launch {
            moveMessageToTrashUseCase(
                idStudent = idStudent,
                idMessage = idMessage
            ).onFailure {

            }
        }
    }

    private fun restoreMessage(idStudent:  Long?, idMessage:  Long) {
        viewModelScope.launch {
            removeMessageFromTrashUseCase(
                idStudent = idStudent,
                idMessage = idMessage
            ).onFailure {

            }
        }
    }

    private fun deleteForever(idStudent:  Long?, idMessage:  Long) {
        viewModelScope.launch {
            deleteMessageForeverUseCase(
                idStudent = idStudent,
                idMessage = idMessage
            ).onFailure {
                Log.e("deleteForever", it.stackTraceToString())
            }
        }
    }

}
