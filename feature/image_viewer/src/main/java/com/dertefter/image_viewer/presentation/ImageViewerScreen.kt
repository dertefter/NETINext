package com.dertefter.image_viewer.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.dertefter.design.components.buttons.AppNavigationIcon
import com.dertefter.design.theme.spacing
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable


@Composable
fun ImageViewerScreen(
    onEvent: (Event) -> Unit,
    imageUrls: List<String>,
    viewPosition: Int?,
) {
    val pagerState = rememberPagerState(
        initialPage = viewPosition ?: 0,
        pageCount = { imageUrls.size }
    )

    Scaffold(
        containerColor = Color.Black
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                pageSpacing = 16.dp,
            ) { page ->
                val painter = rememberAsyncImagePainter(imageUrls[page])
                val zoomState = rememberZoomState(contentSize = painter.intrinsicSize)
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .zoomable(zoomState)
                )
            }

            AppNavigationIcon(
                onClick = {
                    onEvent(Event.OnNavigateBack)
                },
                modifier = Modifier
                    .padding(contentPadding)
                    .padding(MaterialTheme.spacing.defaultScreenPadding),
                containerColor = Color.Black,
                contentColor = Color.White
            )
        }
    }
}
