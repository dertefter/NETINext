package com.dertefter.settings_theme.presentation

import com.dertefter.data.dto.settings.ThemeStyle

sealed class Event {
    object OnNavigateBack : Event()

    data class OnSelectColor(val color: Long?) : Event()

    data class OnSetIsShapeCut(val isCut: Boolean) : Event()

    data class OnSetNewColorSpecVersion(val isNew: Boolean) : Event()

    data class OnSetThemeStyle(val themeStyle: ThemeStyle) : Event()

}
