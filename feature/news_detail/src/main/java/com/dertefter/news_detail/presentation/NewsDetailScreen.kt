package com.dertefter.news_detail.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dertefter.data.common.AppError
import com.dertefter.data.dto.news.NewsDetailDto
import com.dertefter.design.components.PullToRefreshIndicator
import com.dertefter.design.components.appbar.AppToolbar
import com.dertefter.design.components.buttons.AppNavigationIcon
import com.dertefter.design.components.common.ErrorLarge
import com.dertefter.design.components.loading.AppLoadingIndicator
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.isCut
import com.dertefter.design.theme.spacing
import com.dertefter.news_detail.presentation.components.ImagesCarousel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NewsDetailScreen(
    onEvent: (Event) -> Unit,
    newsState: NewsState,
    previewUrl: String?,
    type: String?,
    tags: String?,
    date: String?,
    seedColor: Long? = null,
) {

    AppTheme(
        isCut = MaterialTheme.isCut,
        seedColor = seedColor
    ) {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

        Scaffold(
            topBar = {
                AppToolbar(
                    navigationIcon = {
                        AppNavigationIcon(
                            onClick = {
                                onEvent(Event.OnNavigateBack)
                            },
                        )
                    },
                    actions = {
                        AppNavigationIcon(
                            icon = Icons.Share,
                            onClick = {},
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        )
        { contentPadding ->

            val pullToRefreshState = rememberPullToRefreshState()

            PullToRefreshBox(
                modifier = Modifier
                    .fillMaxSize(),
                state = pullToRefreshState,
                isRefreshing = newsState.isLoading && newsState.newsDetailDto != null,
                onRefresh = { onEvent(Event.RequestLoadingNewsDetail) },
                indicator = {
                    PullToRefreshIndicator(
                        modifier = Modifier
                            .padding(contentPadding)
                            .align(Alignment.TopCenter),
                        state = pullToRefreshState,
                        isRefreshing = newsState.isLoading
                    )
                }
            ) {
                AnimatedContent(
                    targetState = newsState,
                    transitionSpec = {
                        val isInitialLoading =
                            initialState.newsDetailDto == null && targetState.newsDetailDto == null
                        val isContentReady =
                            initialState.newsDetailDto == null && targetState.newsDetailDto != null
                        val isErrorShown =
                            initialState.error == null && targetState.error != null && targetState.newsDetailDto == null

                        if (isInitialLoading || isContentReady || isErrorShown) {
                            (fadeIn(animationSpec = tween(300, delayMillis = 50)) + scaleIn(
                                initialScale = 0.95f,
                                animationSpec = tween(300, delayMillis = 50)
                            )).togetherWith(fadeOut(animationSpec = tween(200)))
                        } else {
                            ContentTransform(
                                targetContentEnter = fadeIn(tween(0)),
                                initialContentExit = fadeOut(tween(0)),
                                sizeTransform = SizeTransform(clip = false)
                            )
                        }
                    },
                    label = "NewsDetailContentAnimation"
                ) { state ->
                    if (state.isLoading && state.newsDetailDto == null) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            AppLoadingIndicator()
                        }
                    } else if (state.error != null && state.newsDetailDto == null) {
                        ErrorLarge(
                            modifier = Modifier.fillMaxSize(),
                            onRetry = { onEvent(Event.RequestLoadingNewsDetail) }
                        )
                    } else {
                        state.newsDetailDto?.let { newsDetail ->
                            LazyColumn(
                                modifier = Modifier
                                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                                    .fillMaxSize(),
                                contentPadding = contentPadding,
                                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {


                                if (previewUrl != null) {
                                    item {
                                        AsyncImage(
                                            model = previewUrl,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .widthIn(max = 480.dp)
                                                .padding(
                                                    horizontal = MaterialTheme.spacing.defaultScreenPadding,
                                                )
                                                .clip(MaterialTheme.shapes.large)
                                                .height(240.dp)
                                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                                .fillMaxWidth(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }

                                }


                                item {
                                    Text(
                                        text = newsDetail.title,
                                        modifier = Modifier
                                            .widthIn(max = 480.dp)
                                            .padding(
                                                horizontal = MaterialTheme.spacing.defaultScreenPadding,
                                            )
                                            .fillMaxWidth(),
                                        style = MaterialTheme.typography.displaySmallEmphasized,
                                    )

                                }

                                item {
                                    Row(
                                        modifier = Modifier
                                            .widthIn(max = 480.dp)
                                            .padding(
                                                horizontal = MaterialTheme.spacing.defaultScreenPadding,
                                                vertical = MaterialTheme.spacing.small
                                            )
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        type?.let { type ->
                                            Text(
                                                text = type,
                                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                                style = MaterialTheme.typography.labelLargeEmphasized,
                                                modifier = Modifier
                                                    .clip(MaterialTheme.shapes.medium)
                                                    .background(MaterialTheme.colorScheme.secondaryContainer)
                                                    .padding(
                                                        vertical = MaterialTheme.spacing.medium,
                                                        horizontal = MaterialTheme.spacing.large
                                                    )
                                            )
                                        }

                                        date?.let { date ->
                                            Text(
                                                text = date,
                                                color = MaterialTheme.colorScheme.onBackground,
                                                style = MaterialTheme.typography.labelLargeEmphasized,
                                                modifier = Modifier
                                                    .padding(
                                                        vertical = MaterialTheme.spacing.medium,
                                                        horizontal = MaterialTheme.spacing.large
                                                    )
                                            )
                                        }

                                    }
                                }
                                newsDetail.contentHtml?.let {
                                    item {
                                        Text(
                                            modifier = Modifier
                                                .widthIn(max = 480.dp)
                                                .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding),
                                            text = AnnotatedString.fromHtml(htmlString = it),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onBackground,
                                        )
                                    }
                                }

                                if (newsDetail.imageUrls.isNotEmpty()) {
                                    item {
                                        ImagesCarousel(
                                            imageUrls = newsDetail.imageUrls,
                                            modifier = Modifier.padding(vertical = MaterialTheme.spacing.defaultScreenPadding),
                                            onImageClick = { _, position ->
                                                onEvent(
                                                    Event.OnNavigateToImageViewer(
                                                        imageUrls = newsDetail.imageUrls,
                                                        viewPosition = position

                                                    )
                                                )

                                            }
                                        )
                                    }
                                }


                            }
                        }
                    }
                }
            }
        }
    }



}

@Preview(showBackground = true, device = "spec:width=673dp,height=841dp")
@Composable
private fun NewsDetailScreenPreview() {
    val sampleNewsDetail = NewsDetailDto(
        title = "НГТУ НЭТИ вошел в число победителей конкурса грантов для популяризации науки",
        contentHtml = "НГТУ НЭТИ вошел в число победителей конкурса грантов для популяризации науки. <br><br> Это отличная новость для всего университета!",
        imageUrls = listOf("","","","")
    )
    val newsState = NewsState(
        newsDetailDto = sampleNewsDetail,
        isLoading = false,
        error = AppError.Unknown
    )
    NewsDetailScreen(
        onEvent = {},
        newsState = newsState,
        previewUrl = "",
        type = "Новости",
        tags = "Гранты, Конкурсы",
        date = "27 октября 2023",

    )
}
