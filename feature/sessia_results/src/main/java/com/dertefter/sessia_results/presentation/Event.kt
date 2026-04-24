package com.dertefter.sessia_results.presentation

sealed class Event {
    object OnUpdateSessiaResults : Event()

    object OnNavigateBack : Event()

    object OnShare : Event()
}
