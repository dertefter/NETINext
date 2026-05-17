package com.dertefter.control_weeks.presentation

sealed class Event {
    object OnUpdateSessiaResults : Event()

    object OnNavigateBack : Event()

    object OnShare : Event()
}
