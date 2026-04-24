package com.dertefter.messages.presentation

sealed class Event {

    object OnUpdateMessages : Event()

    data class OnUpdateFilterMode(val filterMode: FilterMode): Event()

    data class OnMoveToArchive(val idStudent:  Long? = null, val idMessage:  Long): Event()

    data class OnRemoveFromArchive(val idStudent:  Long? = null, val idMessage:  Long): Event()

    data class OnOpenMessageDetail(val idStudent:  Long? = null, val idMessage:  Long): Event()

    data class OnDeleteForever(val idStudent:  Long? = null, val idMessage:  Long): Event()

}
