package com.dertefter.messages.presentation

sealed class FilterMode {
    object ALL : FilterMode()
    object UNREAD : FilterMode()
    object DELETED : FilterMode()
    data class TAB(val tabId: Int) : FilterMode()
}