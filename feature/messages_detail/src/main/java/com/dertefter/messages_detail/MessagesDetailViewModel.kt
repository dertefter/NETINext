package com.dertefter.messages_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.messages_detail.presentation.Event
import com.dertefter.messages_detail.presentation.UiState
import com.dertefter.messages_detail.usecase.DeleteMessageForeverUseCase
import com.dertefter.messages_detail.usecase.GetMessageUseCase
import com.dertefter.messages_detail.usecase.NavigateUpUseCase
import com.dertefter.messages_detail.usecase.ReadMessageUseCase
import com.dertefter.messages_detail.usecase.SetMessageArchivedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagesDetailViewModel @Inject constructor(
    getMessageUseCase: GetMessageUseCase,
    private val readMessageUseCase: ReadMessageUseCase,
    private val setMessageArchivedUseCase: SetMessageArchivedUseCase,
    private val deleteMessageForeverUseCase: DeleteMessageForeverUseCase,
    private val navigateUpUseCase: NavigateUpUseCase,
) : ViewModel() {

    private val _idMessage = MutableStateFlow<Long?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val message = _idMessage.flatMapLatest { id ->
        if (id == null) flowOf(null)
        else getMessageUseCase(id)
    }

    private var idStudent: Long? = null

    private val isDeleting = MutableStateFlow(false)

    private val isLoading = MutableStateFlow(true)

    private val isArchiving = MutableStateFlow(false)

    private val isError = MutableStateFlow(false)

    private var isPanel: Boolean = false

    val uiState: StateFlow<UiState> = combine(
        message,
        isLoading,
        isError,
        isArchiving,
        isDeleting
    ) { message, isLoading, isError, isArchiving, isDeleting ->
        UiState(
            message = message,
            isLoading = isLoading,
            isError = isError,
            isArchiving = isArchiving,
            isDeleting = isDeleting
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState()
    )


    fun initWith(
        idMessage: Long,
        idStudent: Long?,
        isPanel: Boolean
    ) {
        this._idMessage.value = idMessage
        this.idStudent = idStudent
        this.isPanel = isPanel
        readMessage()
    }


    fun onEvent(event: Event) {
        when (event) {
            is Event.OnUpdateMessage -> {
                readMessage()
            }
            is Event.OnMoveToArchive -> {
                archiveMessage(event.isArchived)
            }
            is Event.OnDelete -> {
                deleteMessage()
            }
            is Event.OnNavigateBack -> {
                navigateUpUseCase()
            }
        }
    }

    private fun readMessage(){
        viewModelScope.launch {
            val idMessage = _idMessage.value
            if (idMessage != null){
                isLoading.value = true
                isError.value = false
                readMessageUseCase(idStudent, idMessage).onFailure {
                    isError.value = true
                }
                isLoading.value = false
            }
        }
    }

    private fun archiveMessage(isArchived: Boolean) {
        viewModelScope.launch {
            val id = _idMessage.value ?: return@launch
            isArchiving.value = true
            setMessageArchivedUseCase(idStudent, id, isArchived)
            isArchiving.value = false
        }
    }

    private fun deleteMessage() {
        viewModelScope.launch {
            val id = _idMessage.value ?: return@launch
            isDeleting.value = true
            deleteMessageForeverUseCase(idStudent, id).onSuccess {
                if (!isPanel){
                    navigateUpUseCase()
                }
            }
            isDeleting.value = false
        }
    }

}
