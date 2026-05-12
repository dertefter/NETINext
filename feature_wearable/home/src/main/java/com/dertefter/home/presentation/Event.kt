package com.dertefter.home.presentation

sealed class Event {
    object OnUpdateSchedule : Event()

    object OnOpenDetails : Event()

}