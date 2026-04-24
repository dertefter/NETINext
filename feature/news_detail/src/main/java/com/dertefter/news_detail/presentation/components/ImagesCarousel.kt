package com.dertefter.news_detail.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing

@Composable
fun ImagesCarousel(
    modifier: Modifier = Modifier,
    imageUrls: List<String>,
    onImageClick: (String, Int) -> Unit
) {
    val carouselState = rememberCarouselState { imageUrls.count() }

    HorizontalUncontainedCarousel(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        state = carouselState,
        itemWidth  = 186.dp,
        itemSpacing = 8.dp,
        contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.defaultScreenPadding)
    ) { index ->
        val imageUrl = imageUrls[index]
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .height(205.dp)
                .maskClip(MaterialTheme.shapes.extraLarge)
                .clickable { onImageClick(imageUrl, index) }
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentScale = ContentScale.Crop
        )
    }
}
