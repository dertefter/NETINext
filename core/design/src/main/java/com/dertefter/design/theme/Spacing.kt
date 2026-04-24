package com.dertefter.design.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Spacing(
    val extraExtraSmall: Dp = 1.dp,
    val extraSmall: Dp = 2.dp,
    val small: Dp = 4.dp,
    val medium: Dp = 8.dp,
    val large: Dp = 12.dp,
    val extraLarge: Dp = 16.dp,

    val defaultScreenPadding: Dp = large,
    val defaultItemInnerPadding: Dp = medium,
    val defaultSpaceBetweenLists: Dp = small
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }
