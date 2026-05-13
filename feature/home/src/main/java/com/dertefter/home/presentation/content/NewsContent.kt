package com.dertefter.home.presentation.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.dertefter.data.dto.news.NewsItem
import com.dertefter.design.components.common.ErrorCard
import com.dertefter.design.components.loading.AppLoadingIndicator
import com.dertefter.design.theme.cornerShape
import com.dertefter.design.theme.rounding
import com.dertefter.design.theme.spacing
import com.dertefter.home.presentation.Event
import com.dertefter.home.presentation.components.NewsCard

fun LazyListScope.newsContent(
    lazyNewsItems: LazyPagingItems<NewsItem>,
    onEvent: (Event) -> Unit
) {

    items(
        count = lazyNewsItems.itemCount,
        key = lazyNewsItems.itemKey { "news_${it.id}" }
    ) { index ->
        val newsItem = lazyNewsItems[index]
        if (newsItem != null) {

            val highRadius = MaterialTheme.rounding.largeIncreased
            val smallRadius = MaterialTheme.rounding.small

            val shape = if (index == 0) {
                MaterialTheme.cornerShape(
                    topStart = highRadius,
                    topEnd = highRadius,
                    bottomStart = smallRadius,
                    bottomEnd = smallRadius
                )
            } else {
                MaterialTheme.cornerShape(smallRadius)
            }

            NewsCard(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding),
                newsItem = newsItem,
                shape = shape,
                onClick = { contentColor ->
                    onEvent(
                        Event.OnNewsClick(
                            newsItem.id,
                            newsItem.imageUrl,
                            newsItem.type,
                            newsItem.tags,
                            newsItem.date,
                            contentColor,
                            newsItem.detailUrl

                        )
                    )
                }
            )
        }
    }

    when (lazyNewsItems.loadState.append) {
        is androidx.paging.LoadState.Loading -> {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AppLoadingIndicator()
                }
            }
        }
        is androidx.paging.LoadState.Error -> {
            item {
                ErrorCard(
                    onRetry = {
                        lazyNewsItems.retry()
                    }
                )
            }

        }
        else -> {}
    }
}
