package com.dertefter.neticlient.presentation

import com.dertefter.data.dto.settings.ThemeStyle

data class ThemeState(
    val seedColor: Long? = null,
    val isShapeCut: Boolean = false,
    val themeStyle: ThemeStyle = ThemeStyle.TonalSpot,
    val oldColorSpecVersion: Boolean = true
)