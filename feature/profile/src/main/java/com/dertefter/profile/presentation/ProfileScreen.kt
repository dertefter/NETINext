package com.dertefter.profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.ImageLoader
import coil.imageLoader
import com.dertefter.data.dto.auth.AuthStatus
import com.dertefter.data.dto.user.LksDto
import com.dertefter.data.dto.user.UserInfoDto
import com.dertefter.design.components.PullToRefreshIndicator
import com.dertefter.design.components.appbar.AppToolbar
import com.dertefter.design.components.auth.AuthRequestCard
import com.dertefter.design.components.buttons.AppNavigationIcon
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing
import com.dertefter.navigation.Routes
import com.dertefter.profile.R
import com.dertefter.profile.presentation.components.LksItem
import com.dertefter.profile.presentation.components.UserInfoCard
import com.dertefter.profile.presentation.components.RoutesListMenu

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProfileScreen(
    onEvent: (Event) -> Unit,
    uiState: UiState,
    imageLoader: ImageLoader = LocalContext.current.imageLoader
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            AppToolbar(
                title = stringResource(R.string.profile_title),
                actions = {
                    AppNavigationIcon(
                        icon = Icons.Settings,
                        onClick = {
                            onEvent(
                                Event.OnNavigateToRoute(Routes.Settings)
                            )
                        },
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    )
    { contentPadding ->

        val state = rememberPullToRefreshState()

        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            state = state,
            isRefreshing = uiState.userInfoState.isLoading,
            onRefresh = { onEvent(Event.OnRequestUpdate) },
            indicator = {
                PullToRefreshIndicator(
                    modifier = Modifier
                        .padding(contentPadding)
                        .align(Alignment.TopCenter),
                    state = state,
                    isRefreshing = uiState.userInfoState.isLoading
                )
            }
        ) {

            LazyColumn(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding)
                    .padding(contentPadding),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {

                if (uiState.authStatus is AuthStatus.Unauthorized) {
                    item {
                        AuthRequestCard(
                            onClick = {
                                onEvent(
                                    Event.OnNavigateToRoute(
                                        Routes.Auth
                                    )
                                )
                            }
                        )
                    }

                } else {
                    item {
                        UserInfoCard(
                            name = uiState.userInfoState.userInfo?.name,
                            avatarUrl = uiState.userInfoState.userInfo?.photoPath,
                            login = uiState.authStatus.login.orEmpty(),
                            imageLoader = imageLoader,
                            onClick = {
                                onEvent(Event.OnNavigateToRoute(Routes.ContactInfo))
                            },
                            onImageClick = {
                                uiState.userInfoState.userInfo?.photoPath?.let { photoPath ->
                                    onEvent(
                                        Event.OnNavigateToRoute(
                                            Routes.ImageViewer(
                                                listOf(
                                                    photoPath
                                                )
                                            )
                                        )
                                    )
                                }

                            }
                        )
                    }
                }

                uiState.lksList?.let { lksList ->

                    val selectedItem = lksList.find { it.isSelected }
                    selectedItem?.let { selectedItem ->
                        item {
                            LksItem(
                                title = selectedItem.title,
                                subtitle = selectedItem.subtitle,
                                onClick = {
                                    onEvent(
                                        Event.OnNavigateToRoute(Routes.SwapLks)
                                    )
                                }
                            )
                        }
                    }

                }

                item {
                    RoutesListMenu(
                        routesList = uiState.routesListMenu,
                        onRouteClick = { route ->
                            onEvent(Event.OnNavigateToRoute(route))
                        }
                    )
                }

            }


        }
    }
}



@Preview(showBackground = true, locale = "ru")
@Composable
private fun ProfileScreenUnauthorizedPreview() {
    AppTheme {
        ProfileScreen(
            onEvent = {},
            uiState = UiState(
                authStatus = AuthStatus.Unauthorized,
                userInfoState = UserInfoState(),
                lksList = listOf(
                    LksDto(
                        "tttt", "ttttt", 111, true,
                    )
                )
            ),
            imageLoader = ImageLoader.Builder(LocalContext.current).build()
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun ProfileScreenAuthorizedPreview() {
    AppTheme {
        ProfileScreen(
            onEvent = {},
            uiState = UiState(
                authStatus = AuthStatus.Authorized("ivan_ivanov"),
                userInfoState = UserInfoState(
                    userInfo = UserInfoDto(
                        name = "Иван",
                        surname = "Иванов",
                        patronymic = "Иванович",
                        email = "ivanov@example.com",
                        address = "Новосибирск",
                        mobilePhoneNumber = "+79991234567",
                        symFaculty = "АВТФ",
                        symGroup = "АВТ-123",
                        photoPath = null,
                        birthday = "01.01.2000",

                        )
                ),
                lksList = listOf(
                    LksDto(
                        "tttt", "ttttt", 111, true,
                    )
                )
            ),
            imageLoader = ImageLoader.Builder(LocalContext.current).build()
        )
    }
}



@Preview(showBackground = true)
@Composable
private fun ProfileScreenLoadingPreview() {
    AppTheme {
        ProfileScreen(
            onEvent = {},
            uiState = UiState(
                authStatus = AuthStatus.Loading("ivan_ivanov"),
                userInfoState = UserInfoState(isLoading = true),
                lksList = listOf(
                    LksDto(
                        "tttt", "ttttt", 111, true,
                    )
                )
            ),
            imageLoader = ImageLoader.Builder(LocalContext.current).build()
        )
    }
}
