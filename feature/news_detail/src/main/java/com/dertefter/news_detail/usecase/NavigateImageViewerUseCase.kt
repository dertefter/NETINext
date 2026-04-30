package com.dertefter.news_detail.usecase

import com.dertefter.navigation.Navigator
import com.dertefter.navigation.Routes
import javax.inject.Inject

class NavigateImageViewerUseCase @Inject constructor(

    private val navigator: Navigator,
) {
    operator fun invoke(
        imageUrls: List<String>,
        viewPosition: Int? = null,
    ){
        return navigator.navigate(
            Routes.ImageViewer(
                imageUrls = imageUrls,
                viewPosition = viewPosition
            )

        )
    }
}