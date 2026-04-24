package com.dertefter.design.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import com.dertefter.design.shape.MorphPolygonShape


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ShapeMorphContainer(
    modifier: Modifier = Modifier,
    unpressedShape: RoundedPolygon = remember {
        MaterialShapes.Gem
    },
    pressedShape: RoundedPolygon = remember {
        MaterialShapes.Flower
    },
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    pressedSpeed: Float = 0f,
    unpressedSpeed: Float = 0.3f,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {

    val morph = remember(unpressedShape, pressedShape) {
        Morph(unpressedShape, pressedShape)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedProgress = animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f,
        label = "progress",
        animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessMedium)
    )

    var rotation by remember { mutableFloatStateOf(0f) }
    val speedMultiplier by animateFloatAsState(
        targetValue = if (isPressed) pressedSpeed else unpressedSpeed,
        label = "speed"
    )

    LaunchedEffect(Unit) {
        var lastTime = withFrameNanos { it }
        while (true) {
            val currentTime = withFrameNanos { it }
            val deltaTime = (currentTime - lastTime) / 1_000_000_000f
            rotation = (rotation + deltaTime * 60f * speedMultiplier) % 360f
            lastTime = currentTime
        }
    }

    Box(
        modifier = modifier
            .padding(8.dp)
            .clip(MorphPolygonShape(morph, animatedProgress.value, rotation))
            .background(containerColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        content()
    }

}

@Preview(locale = "en")
@Composable
fun ShapeMorphContainerPreview(){
    MaterialTheme {
        ShapeMorphContainer(
        ){}
    }
}
