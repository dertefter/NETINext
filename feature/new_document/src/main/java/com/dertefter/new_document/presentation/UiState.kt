package com.dertefter.new_document.presentation

import com.dertefter.data.dto.docs.DocumentOptionItem
import com.dertefter.data.dto.docs.DocumentRequestItem

data class UiState(
    val optionList: List<DocumentOptionItem> = emptyList(),
    val selectedOption: DocumentOptionItem? = null,
    val comment: String = "",
    val documentRequest: DocumentRequestItem? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)