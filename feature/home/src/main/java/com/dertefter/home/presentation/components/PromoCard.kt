package com.dertefter.home.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing
import com.materialkolor.ktx.rememberThemeColor
import com.materialkolor.rememberDynamicColorScheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PromoCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    desc: String? = null,
    imageUrl: String? = null,
    onClick: () -> Unit
){

    var imageBitmap by remember(imageUrl) { mutableStateOf<ImageBitmap?>(null) }

    val themeColor = imageBitmap?.let {
        rememberThemeColor(it, fallback = MaterialTheme.colorScheme.surfaceVariant)
    } ?: MaterialTheme.colorScheme.surfaceVariant

    val colorScheme = rememberDynamicColorScheme(themeColor, isDark = true)

    val animatedSurfaceContainer by animateColorAsState(
        targetValue = colorScheme.primaryContainer,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "surfaceContainer"
    )

    val animatedOnPrimaryContainer by animateColorAsState(
        targetValue = colorScheme.onPrimaryContainer,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "onSurface"
    )

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.largeIncreased)
            .clickable(onClick = onClick)
            .height(180.dp)
            .fillMaxWidth()
    ){
        imageUrl?.let {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(it)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .background(animatedOnPrimaryContainer),
                contentScale = ContentScale.Crop,
                onSuccess = { state ->
                    if (imageBitmap == null) {
                        imageBitmap = state.result.drawable.toBitmap().scale(10, 10).asImageBitmap()
                    }
                }
            )
        }

        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            animatedSurfaceContainer.copy(alpha = 0.7f),
                            animatedSurfaceContainer.copy(alpha = 0.3f),
                            animatedSurfaceContainer.copy(alpha = 0.1f),
                        )
                    )
                )
                .fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.extraLarge),
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Column(
            ) {
                title?.let { title ->
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLargeEmphasized,
                        color = animatedOnPrimaryContainer
                    )
                }

                subtitle?.let { subtitle ->
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelLarge,
                        color = animatedOnPrimaryContainer
                    )
                }

            }

            desc?.let { desc ->
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMediumEmphasized,
                    color = animatedOnPrimaryContainer
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PromoCardPreview() {
    AppTheme {
        PromoCard(
            title = "Promo Title",
            subtitle = "Promo Subtitle",
            desc = "This is a description for the promo card.",
            modifier = Modifier.padding(MaterialTheme.spacing.large),
            onClick = {}
        )
    }
}
