package com.dertefter.search_person.presentation

sealed class Event {

    data class OnSearchQueryChanged(val query: String) : Event()

    data class OnOpenPerson(val personId: Long) : Event()

    data object OnNavigateBack : Event()

}
