package com.dertefter.messages_detail.presentation

import com.dertefter.data.dto.messsages.MessageDto

data class UiState(
    val message: MessageDto? = null,
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val isArchiving: Boolean = false,
    val isDeleting: Boolean = false
)