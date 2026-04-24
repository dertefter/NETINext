package com.dertefter.design.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Rounding(
    val none: Dp = 0.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 12.dp,
    val large: Dp = 16.dp,
    val largeIncreased: Dp = 20.dp,
    val extraLarge: Dp = 28.dp,
    val extraLargeIncreased: Dp = 32.dp,
    val extraExtraLarge: Dp = 48.dp,

    val defaultListItemRadius: Dp = small,
    val defaultListItemRadiusEdges: Dp = large,
)

val LocalRounding = staticCompositionLocalOf { Rounding() }
