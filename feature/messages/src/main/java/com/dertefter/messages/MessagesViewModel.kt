package com.dertefter.messages

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.common.AppError
import com.dertefter.data.common.toAppError
import com.dertefter.data.dto.messsages.MessageDto
import com.dertefter.data.repository.MessagesRepository
import com.dertefter.messages.presentation.Event
import com.dertefter.messages.presentation.FilterMode
import com.dertefter.messages.presentation.UiState
import com.dertefter.navigation.Navigator
import com.dertefter.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val messagesRepository: MessagesRepository,
    private val navigator: Navigator
) : ViewModel() {

    private val _isUpdating = MutableStateFlow(false)
    private val _error = MutableStateFlow<AppError?>(null)
    private val _messages: Flow<List<MessageDto>> = messagesRepository.getMessagesFlow()

    private val _filterMode = MutableStateFlow<FilterMode>(FilterMode.ALL)

    val uiState: StateFlow<UiState> = combine(
        _messages,
        _isUpdating,
        _error,
        _filterMode
    ) { messages, isUpdating, error, filterMode ->
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
            filterMode = filterMode
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
                navigator.navigate(
                    Routes.MessagesDetail(
                        idStudent = event.idStudent,
                        idMessage = event.idMessage
                    )
                )
            }

        }
    }

    private fun updateMessages() {
        viewModelScope.launch {
            _isUpdating.value = true
            _error.value = null
            messagesRepository.updateMessages().onFailure {
                _error.value = it.toAppError()
            }
            _isUpdating.value = false
        }
    }

    private fun archiveMessage(idStudent:  Long?, idMessage:  Long) {
        viewModelScope.launch {
            messagesRepository.moveMessageToTrash(
                idStudent = idStudent,
                idMessage = idMessage
            ).onFailure {

            }
        }
    }

    private fun restoreMessage(idStudent:  Long?, idMessage:  Long) {
        viewModelScope.launch {
            messagesRepository.removeMessageFromTrash(
                idStudent = idStudent,
                idMessage = idMessage
            ).onFailure {

            }
        }
    }

    private fun deleteForever(idStudent:  Long?, idMessage:  Long) {
        viewModelScope.launch {
            messagesRepository.deleteMessageForever(
                idStudent = idStudent,
                idMessage = idMessage
            ).onFailure {
                Log.e("deleteForever", it.stackTraceToString())
            }
        }
    }

}
