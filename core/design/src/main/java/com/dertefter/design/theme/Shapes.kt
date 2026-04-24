package com.dertefter.design.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Shapes
import androidx.compose.runtime.staticCompositionLocalOf

val LocalIsCut = staticCompositionLocalOf { false }

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
val RoundedShapes = Shapes(
    extraSmall = RoundedCornerShape(Rounding().extraSmall),
    small = RoundedCornerShape(Rounding().small),
    medium = RoundedCornerShape(Rounding().medium),
    large = RoundedCornerShape(Rounding().large),
    extraLarge = RoundedCornerShape(Rounding().extraLarge),
    largeIncreased = RoundedCornerShape(Rounding().largeIncreased),
    extraLargeIncreased = RoundedCornerShape(Rounding().extraLargeIncreased),
    extraExtraLarge = RoundedCornerShape(Rounding().extraExtraLarge),
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
val CutShapes = Shapes(
    extraSmall = CutCornerShape(Rounding().extraSmall),
    small = CutCornerShape(Rounding().small),
    medium = CutCornerShape(Rounding().medium),
    large = CutCornerShape(Rounding().large),
    extraLarge = CutCornerShape(Rounding().extraLarge),
    largeIncreased = CutCornerShape(Rounding().largeIncreased),
    extraLargeIncreased = CutCornerShape(Rounding().extraLargeIncreased),
    extraExtraLarge = CutCornerShape(Rounding().extraExtraLarge),
)
