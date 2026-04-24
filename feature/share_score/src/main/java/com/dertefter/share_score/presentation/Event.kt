package com.dertefter.share_score.presentation

sealed class Event {
    object OnUpdateLink : Event()

    object OnRegenerateLink : Event()
}
