package com.dertefter.design.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.cornerShape

@Composable
fun CarouselItem(
    modifier: Modifier = Modifier,
    isFocused: Boolean = true,
    shape: CornerBasedShape = MaterialTheme.cornerShape(8.dp),
    content: @Composable () -> Unit
) {
    val contentAlpha by animateFloatAsState(
        targetValue = if (isFocused) 1f else 0f,
        label = "carouselItemAlpha"
    )

    Box(){

        Box(
            modifier = modifier
                .clip(shape)
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .alpha(contentAlpha)
        ) {
            content()
        }

    }


}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview()
@Composable
fun CarouselItemPreview() {
    AppTheme {
        CarouselItem(
            modifier = Modifier.size(250.dp, 150.dp),
            isFocused = true,
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Carousel Item Content",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview()
@Composable
fun CarouselItemNotFocusedPreview() {
    AppTheme {
        CarouselItem(
            modifier = Modifier.size(56.dp, 150.dp),
            isFocused = false,
            shape = MaterialTheme.cornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Carousel Item Content",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

