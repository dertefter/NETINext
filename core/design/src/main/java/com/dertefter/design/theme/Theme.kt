package com.dertefter.design.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.materialkolor.DynamicMaterialExpressiveTheme
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamiccolor.ColorSpec
import com.materialkolor.ktx.harmonize
import com.materialkolor.rememberDynamicMaterialThemeState
import kotlinx.coroutines.delay

private val DarkColorScheme = darkColorScheme()

private val LightColorScheme = lightColorScheme()

val LocalIsFold = staticCompositionLocalOf { false }

val LocalIsTab = staticCompositionLocalOf { false }

val LocalSeedColor = staticCompositionLocalOf<Long?> { null }

val LocalIsLessonHintExpanded = staticCompositionLocalOf { true }

@Immutable
data class CustomColors(
    val success: Color = Color.Unspecified,
    val onSuccess: Color = Color.Unspecified,
    val successContainer: Color = Color.Unspecified,
    val onSuccessContainer: Color = Color.Unspecified,
)

val LocalCustomColors = staticCompositionLocalOf { CustomColors() }

val MaterialTheme.spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current

val MaterialTheme.rounding: Rounding
    @Composable
    @ReadOnlyComposable
    get() = LocalRounding.current

val MaterialTheme.customColors: CustomColors
    @Composable
    @ReadOnlyComposable
    get() = LocalCustomColors.current

val MaterialTheme.isFold: Boolean
    @Composable
    @ReadOnlyComposable
    get() = LocalIsFold.current

val MaterialTheme.isTab: Boolean
    @Composable
    @ReadOnlyComposable
    get() = LocalIsTab.current

val MaterialTheme.seedColor: Long?
    @Composable
    @ReadOnlyComposable
    get() = LocalSeedColor.current

val MaterialTheme.isLessonHintExpanded: Boolean
    @Composable
    @ReadOnlyComposable
    get() = LocalIsLessonHintExpanded.current

val MaterialTheme.isCut: Boolean
    @Composable
    @ReadOnlyComposable
    get() = LocalIsCut.current

@Composable
fun MaterialTheme.cornerShape(
    topStart: Dp = 0.dp,
    topEnd: Dp = 0.dp,
    bottomEnd: Dp = 0.dp,
    bottomStart: Dp = 0.dp
): CornerBasedShape {
    return if (LocalIsCut.current) {
        CutCornerShape(topStart, topEnd, bottomEnd, bottomStart)
    } else {
        RoundedCornerShape(topStart, topEnd, bottomEnd, bottomStart)
    }
}


@Composable
fun MaterialTheme.cornerShape(size: Dp): CornerBasedShape {
    return if (LocalIsCut.current) {
        CutCornerShape(size)
    } else {
        RoundedCornerShape(size)
    }
}

@Composable
fun MaterialTheme.circleShape(): Shape {
    return if (isCut) {
        CutCornerShape(25)
    } else {
        CircleShape
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    isPanel: Boolean = false,
    seedColor: Long? = null,
    isCut: Boolean = false,
    content: @Composable () -> Unit
) {

    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val widthInDp = with(density) { windowInfo.containerSize.width.toDp() }

    val isFold = widthInDp > 600.dp
    val isTab = widthInDp > 840.dp

    val context = LocalContext.current

    var isLessonHintExpanded by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(8000L)
        isLessonHintExpanded = false
    }

    val colorScheme = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (darkTheme) {
            dynamicDarkColorScheme(context)
        } else {
            dynamicLightColorScheme(context)
        }
    } else {
        if (darkTheme) {
            DarkColorScheme
        } else {
            LightColorScheme
        }
    }

    val dynamicThemeState = if (seedColor == null) {
        rememberDynamicMaterialThemeState(
            isDark = darkTheme,
            style = PaletteStyle.TonalSpot,
            specVersion = ColorSpec.SpecVersion.SPEC_2021,
            seedColor = colorScheme.primary,
            primary = colorScheme.primary,
            secondary = colorScheme.secondary,
            tertiary = colorScheme.tertiary,
        )
    } else {
        rememberDynamicMaterialThemeState(
            isDark = darkTheme,
            style = PaletteStyle.TonalSpot,
            specVersion = ColorSpec.SpecVersion.SPEC_2021,
            seedColor = Color(seedColor),
        )
    }

    CompositionLocalProvider(
        LocalSpacing provides Spacing(),
        LocalRounding provides Rounding(),
        LocalIsFold provides isFold,
        LocalIsTab provides isTab,
        LocalIsCut provides isCut,
        LocalSeedColor provides seedColor,
        LocalIsLessonHintExpanded provides isLessonHintExpanded
    ) {
        DynamicMaterialExpressiveTheme(
            state = dynamicThemeState,
            motionScheme = MotionScheme.expressive(),
            animate = true,
            typography = Typography,
            shapes = if (isCut) CutShapes else RoundedShapes
        ) {
            val successColor =
                Color.Green.harmonize(MaterialTheme.colorScheme.secondary, matchSaturation = true)
            val onSuccessColor =
                Color.Green.harmonize(MaterialTheme.colorScheme.onSecondary, matchSaturation = true)
            val successContainer =
                Color.Green.harmonize(
                    MaterialTheme.colorScheme.secondaryContainer,
                    matchSaturation = true
                )
            val onSuccessContainer =
                Color.Green.harmonize(
                    MaterialTheme.colorScheme.onSecondaryContainer,
                    matchSaturation = true
                )


            val customColors =
                remember(successColor, onSuccessColor, successContainer, onSuccessContainer) {
                    CustomColors(
                        success = successColor,
                        onSuccess = onSuccessColor,
                        successContainer = successContainer,
                        onSuccessContainer = onSuccessContainer
                    )
                }
            CompositionLocalProvider(
                LocalCustomColors provides customColors,
                content = content
            )
        }
    }
}
