package com.dertefter.doc_detail.presentation

sealed class Event {

    data class OnCheckCancelable(val docNumber: String) : Event()

    data class OnCancelClaim(val docNumber: String) : Event()

    data object OnNavigateUp : Event()

}
