package com.dertefter.design.components.adaptive

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.isTab
import kotlin.math.abs

@Composable
fun PanelsLayout(
    modifier: Modifier = Modifier,
    contentLeft: @Composable () -> Unit,
    contentRight: @Composable () -> Unit
) {

    val isTab = MaterialTheme.isTab

    val minRatio = 0.3f
    val maxRatio = 0.7f
    val minDragRatio = minRatio - 0.05f
    val maxDragRatio = maxRatio + 0.05f

    var manualRatio by remember { mutableStateOf<Float?>(null) }
    val currentRatio = manualRatio ?: 0.5f

    var isPressed by remember { mutableStateOf(false) }
    var isDragging by remember { mutableStateOf(false) }

    val animatedRatio by animateFloatAsState(
        targetValue = currentRatio,
        animationSpec = if (isDragging) snap() else MaterialTheme.motionScheme.defaultSpatialSpec(),
        label = "ratioAnimation"
    )

    val handleColor by animateColorAsState(
        targetValue = if (isPressed || isDragging)
            MaterialTheme.colorScheme.outline
        else
            MaterialTheme.colorScheme.outlineVariant,
        label = "handleColorAnimation"
    )

    var componentWidth by remember { mutableFloatStateOf(0f) }

    val leftWeight = animatedRatio.coerceIn(0.01f, 0.99f)

    Row(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                componentWidth = coordinates.size.width.toFloat()
            }
    ) {
        Box(
            modifier = Modifier
                .weight(leftWeight)
                .consumeWindowInsets(WindowInsets.systemBars.only(WindowInsetsSides.Right))
        ) {
            contentLeft()
        }

        if (isTab) {
            Box(
                modifier = Modifier
                    .width(16.dp)
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                isPressed = true
                                try {
                                    awaitRelease()
                                } finally {
                                    isPressed = false
                                }
                            }
                        )
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { isDragging = true },
                            onDragEnd = {
                                isDragging = false
                                val ratioToSnap = manualRatio ?: 0.5f
                                val snapPoints = listOf(minRatio, 0.5f, maxRatio)
                                val closestSnapPoint = snapPoints.minBy { abs(ratioToSnap - it) }

                                manualRatio = if (closestSnapPoint == 0.5f) null else closestSnapPoint
                            },
                            onDragCancel = { isDragging = false },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                val ratioToUse = manualRatio ?: 0.5f
                                if (componentWidth > 0) {
                                    manualRatio = (ratioToUse + dragAmount.x / componentWidth).coerceIn(
                                        minDragRatio,
                                        maxDragRatio
                                    )
                                }
                            }
                        )
                    }
            ) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight(0.15f)
                        .background(
                            color = handleColor,
                            shape = MaterialTheme.shapes.small
                        )
                        .align(Alignment.Center)
                )
            }
        } else {
            VerticalDivider(
                modifier = Modifier.fillMaxHeight(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
        }

        Box(
            modifier = Modifier
                .weight(1f - leftWeight)
                .background(MaterialTheme.colorScheme.background)
                .consumeWindowInsets(WindowInsets.systemBars.only(WindowInsetsSides.Left))
        ) {
            contentRight()
        }
    }

}



@Preview(showBackground = true, device = "spec:width=673dp,height=841dp")
@Composable
fun PanelsLayoutPreview2() {
    AppTheme {
        PanelsLayout(
            contentLeft = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Left Panel",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            },
            contentRight = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Right Panel",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        )
    }
}
