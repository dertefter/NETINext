package com.dertefter.new_document.presentation

import com.dertefter.data.dto.docs.DocumentOptionItem

sealed class Event {
    object OnUpdateOptions : Event()
    data class OnSelectOption(val option: DocumentOptionItem) : Event()

    data class OnCommentChanged(val s: String) : Event()

    object OnConfirmClaim : Event()

    object OnNavigateUp : Event()
}
