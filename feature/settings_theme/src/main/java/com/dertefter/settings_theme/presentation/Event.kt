package com.dertefter.settings_theme.presentation

sealed class Event {
    object OnNavigateBack : Event()

    data class OnSelectColor(val color: Long?) : Event()

    data class OnSetIsShapeCut(val isCut: Boolean) : Event()

}
