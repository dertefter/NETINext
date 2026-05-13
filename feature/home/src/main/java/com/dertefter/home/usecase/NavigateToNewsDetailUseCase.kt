package com.dertefter.home.usecase

import com.dertefter.navigation.Navigator
import com.dertefter.navigation.Routes
import javax.inject.Inject

class NavigateToNewsDetailUseCase @Inject constructor(
    private val navigator: Navigator
) {
    operator fun invoke(
        newsId: String,
        previewUrl: String? = null,
        type: String? = null,
        tags: String? = null,
        date: String? = null,
        contentColor: Long? = null,
        link: String? = null,
    ) {
        navigator.navigate(
            Routes.NewsDetail(
                newsId = newsId,
                previewUrl = previewUrl,
                type = type,
                tags = tags,
                date = date,
                contentColor = contentColor,
                link = link
            )
        )
    }
}
