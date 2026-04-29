package com.dertefter.swap_lks.presentation

sealed class Event {
    object OnUpdateLks : Event()

    data class OnSetLks(val lksId: Int) : Event()
}
