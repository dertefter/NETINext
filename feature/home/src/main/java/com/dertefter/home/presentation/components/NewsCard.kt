package com.dertefter.home.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dertefter.data.dto.news.NewsItem
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing
import com.materialkolor.ktx.rememberThemeColor
import com.materialkolor.rememberDynamicColorScheme


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NewsCard(
    modifier: Modifier = Modifier,
    newsItem: NewsItem,
    shape: Shape = MaterialTheme.shapes.small,
    onClick: (Long?) -> Unit = {}
) {
    var imageBitmap by remember(newsItem.imageUrl) { mutableStateOf<ImageBitmap?>(null) }

    val themeColor = imageBitmap?.let {
        rememberThemeColor(it, fallback = MaterialTheme.colorScheme.surfaceVariant)
    } ?: MaterialTheme.colorScheme.surfaceVariant

    val colorScheme = rememberDynamicColorScheme(themeColor, isDark = isSystemInDarkTheme())

    val surfaceContainer by animateColorAsState(
        targetValue = colorScheme.surfaceContainer,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "surfaceContainer"
    )
    val onSurface by animateColorAsState(
        targetValue = colorScheme.onSurface,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "onSurface"
    )
    val primary by animateColorAsState(
        targetValue = colorScheme.primary,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "primary"
    )
    val surfaceVariant by animateColorAsState(
        targetValue = colorScheme.surfaceVariant,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "surfaceVariant"
    )

    Box(
        modifier = modifier
            .clip(shape)
            .clickable(onClick = {
                onClick(themeColor.toArgb().toLong())
            }
            )
            .fillMaxWidth()
            .background(surfaceContainer),
    ) {
        Column {
            newsItem.imageUrl?.let {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(surfaceVariant),
                    contentScale = ContentScale.Crop,
                    onSuccess = { state ->
                        if (imageBitmap == null) {
                            imageBitmap =
                                state.result.drawable.toBitmap().scale(10, 10).asImageBitmap()
                        }
                    }
                )
            }
            Column(
                modifier = Modifier.padding(MaterialTheme.spacing.large),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {
                Text(
                    text = newsItem.title,
                    style = MaterialTheme.typography.titleMediumEmphasized,
                    color = onSurface
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = newsItem.date,
                        style = MaterialTheme.typography.labelMedium,
                        color = onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = newsItem.type,
                        style = MaterialTheme.typography.labelMediumEmphasized,
                        color = primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsCardPreview() {
    val sampleNewsItem = NewsItem(
        id = "1",
        type = "news",
        title = "НГТУ НЭТИ вошел в число победителей конкурса грантов для популяризации науки",
        tags = "Гранты, Конкурсы",
        date = "27 октября 2023",
        detailUrl = "https://www.nstu.ru/news/news_more_12345",

        imageUrl = "",
    )
    AppTheme {
        NewsCard(newsItem = sampleNewsItem, modifier = Modifier.padding(12.dp))
    }
}
