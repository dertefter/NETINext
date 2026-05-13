package com.dertefter.neticlient.presentation

import com.dertefter.data.dto.settings.ThemeStyle

data class ThemeState(
    val seedColor: Long? = null,
    val isShapeCut: Boolean? = null,
    val themeStyle: ThemeStyle? = null,
    val oldColorSpecVersion: Boolean? = null
)