package com.dertefter.home.presentation

 import android.content.Intent
 import android.net.Uri
 import androidx.compose.animation.AnimatedVisibility
 import androidx.compose.animation.fadeIn
 import androidx.compose.animation.fadeOut
 import androidx.compose.foundation.layout.Arrangement
 import androidx.compose.foundation.layout.PaddingValues
 import androidx.compose.foundation.layout.Spacer
 import androidx.compose.foundation.layout.WindowInsets
 import androidx.compose.foundation.layout.asPaddingValues
 import androidx.compose.foundation.layout.fillMaxSize
 import androidx.compose.foundation.layout.fillMaxWidth
 import androidx.compose.foundation.layout.height
 import androidx.compose.foundation.layout.padding
 import androidx.compose.foundation.layout.statusBars
 import androidx.compose.foundation.lazy.LazyColumn
 import androidx.compose.foundation.lazy.rememberLazyListState
 import androidx.compose.foundation.pager.HorizontalPager
 import androidx.compose.foundation.pager.rememberPagerState
 import androidx.compose.material3.ExperimentalMaterial3Api
 import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
 import androidx.compose.material3.ExtendedFloatingActionButton
 import androidx.compose.material3.Icon
 import androidx.compose.material3.MaterialTheme
 import androidx.compose.material3.Scaffold
 import androidx.compose.material3.Text
 import androidx.compose.material3.TopAppBarDefaults
 import androidx.compose.material3.pulltorefresh.PullToRefreshBox
 import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
 import androidx.compose.material3.rememberTopAppBarState
 import androidx.compose.runtime.Composable
 import androidx.compose.runtime.derivedStateOf
 import androidx.compose.runtime.getValue
 import androidx.compose.runtime.remember
 import androidx.compose.runtime.rememberCoroutineScope
 import androidx.compose.ui.Alignment
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.input.nestedscroll.nestedScroll
 import androidx.compose.ui.platform.LocalContext
 import androidx.compose.ui.res.stringResource
 import androidx.compose.ui.tooling.preview.Preview
 import androidx.compose.ui.unit.Dp
 import androidx.paging.LoadState
 import androidx.paging.compose.collectAsLazyPagingItems
 import com.dertefter.home.R
 import com.dertefter.data.dto.news.NewsItem
 import com.dertefter.data.dto.news.PromoItem
 import com.dertefter.data.dto.schedule.GroupDto
 import com.dertefter.data.dto.schedule.LessonDto
 import com.dertefter.data.dto.schedule.TimeSlotDto
 import com.dertefter.design.components.PullToRefreshIndicator
 import com.dertefter.design.components.adaptive.PanelsLayout
 import com.dertefter.design.icons.Icons
 import com.dertefter.design.theme.AppTheme
 import com.dertefter.design.theme.rounding
 import com.dertefter.design.theme.spacing
 import com.dertefter.home.presentation.components.PromoCard
 import com.dertefter.home.presentation.content.newsContent
 import com.dertefter.home.presentation.content.scheduleContent
 import com.gigamole.composefadingedges.FadingEdgesGravity
 import com.gigamole.composefadingedges.verticalFadingEdges
 import kotlinx.coroutines.launch
 import java.time.LocalDate
 import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreenFold(
    onEvent: (Event) -> Unit,
    newsState: NewsState,
    scheduleState: ScheduleState,
    promo: List<PromoItem> = emptyList()
) {

    PanelsLayout(
        contentLeft = {
            Scaffold(){ contentPadding ->
                val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    contentPadding = contentPadding,
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ){
                    item{
                        Spacer(
                            modifier = Modifier.height(MaterialTheme.spacing.defaultScreenPadding)
                        )
                    }
                    scheduleContent(scheduleState, onEvent)
                }
            }
        },
        contentRight = {
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current
            val lazyNewsItems = newsState.newsPagingData.collectAsLazyPagingItems()
            val listState = rememberLazyListState()
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
            val showButton by remember {
                derivedStateOf {
                    listState.firstVisibleItemIndex > 10
                }
            }

            val expandButton by remember {
                derivedStateOf {
                    listState.firstVisibleItemIndex < 40
                }
            }

            Scaffold(
                floatingActionButton = {
                    AnimatedVisibility(
                        visible = showButton,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        ExtendedFloatingActionButton(
                            onClick = {
                                coroutineScope.launch {
                                    listState.animateScrollToItem(0)
                                }
                            },
                            expanded = expandButton,
                            icon = { Icon(Icons.ArrowWarmUp, "Up") },
                            text = { Text(text = stringResource(R.string.home_scroll_to_top)) },
                        )
                    }
                }
            ){ contentPadding ->



                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    contentPadding = contentPadding,
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                )
                {

                    if (promo.isNotEmpty()){
                        item(key = "promo_pager") {
                            val pagerState = rememberPagerState(pageCount = { promo.size })
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier
                                    .padding(top = MaterialTheme.spacing.defaultScreenPadding)
                                    .fillMaxWidth(),
                                pageSpacing = MaterialTheme.spacing.small,
                                contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.defaultScreenPadding)
                            ) { page ->
                                val item = promo[page]
                                PromoCard(
                                    title = item.title,
                                    subtitle = item.subtitle,
                                    desc = item.desc,
                                    imageUrl = item.imageUrl,
                                    onClick = {
                                        item.link?.let { url ->
                                            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                            context.startActivity(intent)
                                        }
                                    }
                                )
                            }
                        }
                    }

                    item(key = "news_title") {
                        Text(
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .padding(top = MaterialTheme.spacing.small)
                                .padding(vertical = MaterialTheme.spacing.medium, horizontal = MaterialTheme.rounding.medium),
                            text = stringResource(R.string.home_news_title)
                        )
                    }

                    newsContent(lazyNewsItems, onEvent)

                }
            }
        }
    )


}

@Preview(showBackground = true, device = "spec:width=673dp,height=841dp")
@Composable
private fun HomeScreenFoldPre() {
    val sampleNewsItem = NewsItem(
        id = "1",
        type = "Новости",
        title = "НГТУ НЭТИ вошел в число победителей конкурса грантов для популяризации науки",
        tags = "Гранты, Конкурсы",
        date = "27 октября 2023",
        detailUrl = "https://www.nstu.ru/news/news_more_12345",
        imageUrl = null,
    )

    val sampleTimeSlot = TimeSlotDto(
        dateString = LocalDate.now().toString(),
        startTimeString = "08:30",
        endTimeString = "10:00",
        lessons = listOf(
            LessonDto(
                name = "Высшая математика",
                type = "Лекция",
                aud = "301",
                persons = null
            )
        )
    )

    AppTheme {
        HomeScreenFold(
            newsState = NewsState(),
            scheduleState = ScheduleState(
                date = LocalDate.now(),
                timeSlots = listOf(sampleTimeSlot),
                group = GroupDto("f")
            ),
            onEvent = {}
        )
    }
}
