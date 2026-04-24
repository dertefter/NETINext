package com.dertefter.image_viewer

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dertefter.image_viewer.presentation.ImageViewerScreen

@Composable
fun ImageViewerRoute(
    imageUrls: List<String>,
    viewPosition: Int? = null,
    viewModel: ImageViewerViewModel = hiltViewModel(),
) {

    ImageViewerScreen(
        onEvent = { event ->
            viewModel.onEvent(event)
        },
        viewPosition = viewPosition,
        imageUrls = imageUrls
    )


}
