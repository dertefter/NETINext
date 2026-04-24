package com.dertefter.design.theme

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
val Typography = Typography(
    displayLarge = Typography().displayLarge,
    displayMedium = Typography().displayMedium,
    displaySmall = Typography().displaySmall,
    headlineLarge = Typography().headlineLarge,
    headlineMedium = Typography().headlineMedium,
    headlineSmall = Typography().headlineSmall,
    titleLarge = Typography().titleLarge,
    titleMedium = Typography().titleMedium,
    titleSmall = Typography().titleSmall,
    bodyLarge = Typography().bodyLarge,
    bodyMedium = Typography().bodyMedium,
    bodySmall = Typography().bodySmall,
    labelLarge = Typography().labelLarge,
    labelMedium = Typography().labelMedium,
    labelSmall = Typography().labelSmall,

    // Emphasized
    displayLargeEmphasized = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize =  57.sp,
        lineHeight =  64.0.sp,
        letterSpacing =  0.sp,
    ),

    displayMediumEmphasized = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize =  45.sp,
        lineHeight = 52.0.sp,
        letterSpacing =  0.sp,
    ),


    displaySmallEmphasized = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight =  FontWeight.Medium,
        fontSize =  36.sp,
        lineHeight =  44.0.sp,
        letterSpacing = 0.sp,
    ),

    headlineLargeEmphasized = TextStyle(
        fontFamily =  FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp,
        lineHeight = 40.0.sp,
        letterSpacing = 0.sp,
    ),


    headlineMediumEmphasized = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp,
        lineHeight = 36.0.sp,
        letterSpacing = 0.sp,
    ),

    headlineSmallEmphasized = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 32.0.sp,
        letterSpacing = 0.sp,
    ),
    titleLargeEmphasized = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize =  22.sp,
        lineHeight = 28.0.sp,
        letterSpacing =  0.sp,
    ),

    titleMediumEmphasized = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight =  FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.0.sp,
        letterSpacing = 0.15.sp,
    ),

    titleSmallEmphasized = TextStyle(
        fontFamily =FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.0.sp,
        letterSpacing = 0.1.sp,
    ),
    bodyLargeEmphasized = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.0.sp,
        letterSpacing = 0.15.sp,
    ),
    bodyMediumEmphasized = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.0.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmallEmphasized = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.0.sp,
        letterSpacing = 0.4.sp,
    ),
    labelLargeEmphasized = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.0.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMediumEmphasized = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.0.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmallEmphasized = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        lineHeight = 16.0.sp,
        letterSpacing = 0.5.sp,
    ),
)