package com.dertefter.person_detail.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.ImageLoader
import coil.imageLoader
import com.dertefter.data.dto.person.PersonDetailDto
import com.dertefter.design.components.PullToRefreshIndicator
import com.dertefter.design.components.appbar.AppToolbar
import com.dertefter.design.components.buttons.AppNavigationIcon
import com.dertefter.design.components.common.ErrorLarge
import com.dertefter.design.theme.AppTheme
import com.dertefter.person_detail.presentation.component.PersonDetailContent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PersonDetailScreen(
    onEvent: (Event) -> Unit,
    uiState: UiState,
    imageLoader: ImageLoader = LocalContext.current.imageLoader,
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
                scrollBehavior = scrollBehavior
            )
        }
    ) { contentPadding ->

        val pullToRefreshState = rememberPullToRefreshState()

        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize(),
            state = pullToRefreshState,
            isRefreshing = uiState.isLoading && uiState.personDetail != null,
            onRefresh = { onEvent(Event.OnUpdatePersonDetail) },
            indicator = {
                PullToRefreshIndicator(
                    modifier = Modifier
                        .padding(contentPadding)
                        .align(Alignment.TopCenter),
                    state = pullToRefreshState,
                    isRefreshing = uiState.isLoading
                )
            }
        ) {
            if (uiState.isLoading && uiState.personDetail == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LoadingIndicator()
                }
            } else if (uiState.error != null && uiState.personDetail == null) {
                ErrorLarge(
                    modifier = Modifier.fillMaxSize(),
                    onRetry = { onEvent(Event.OnUpdatePersonDetail) }
                )
            } else {
                uiState.personDetail?.let { personDetail ->
                    PersonDetailContent(
                        personDetail = personDetail,
                        contentPadding = contentPadding,
                        scrollBehavior = scrollBehavior,
                        imageLoader = imageLoader
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Preview(showBackground = true)
@Composable
private fun PersonDetailScreenPreview() {
    AppTheme {
        PersonDetailScreen(
            onEvent = {},
            uiState = UiState(
                personDetail = PersonDetailDto(
                    personId = 1,
                    name = "Иван Иванов",
                    avatarUrl = null,
                    post = "Профессор кафедры математики",
                    contactInfo = listOf("ivanov@university.edu", "+7 (999) 123-45-67"),
                    disciplines = listOf("Высшая математика", "Теория вероятностей"),
                    profiles = listOf("https://scholar.google.com/ivanov"),
                    hasTimetable = true
                ),
                isLoading = false,
                error = null
            )
        )
    }
}

