package com.dertefter.settings_theme.presentation

import com.dertefter.data.dto.settings.ThemeStyle

data class UiState (
    val color: Long? = null,
    val isShapeCut: Boolean = false,
    val themeStyle: ThemeStyle = ThemeStyle.TonalSpot,
    val newColorSpecVersion: Boolean = true
)
