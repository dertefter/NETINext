package com.dertefter.neticlient.presentation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dertefter.data.dto.auth.AuthStatus
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.cornerShape
import com.dertefter.design.theme.isTab
import com.dertefter.design.theme.rounding
import com.dertefter.lesson_detail.LessonDetailRoute
import com.dertefter.navigation.NavigationAction
import com.dertefter.navigation.Navigator
import com.dertefter.navigation.Routes
import com.dertefter.neticlient.navigation.authorizedNavigationItems
import com.dertefter.neticlient.navigation.guestNavigationItems
import com.dertefter.neticlient.presentation.content.PhoneUi
import com.dertefter.neticlient.presentation.content.TabUI
import com.dertefter.search_group.SearchGroupRoute
import com.dertefter.share_score.ShareScoreRoute
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navigator: Navigator,
    screenState: MainScreenState,
    onEvent: (Event) -> Unit,
) {

    AppTheme(
        seedColor = screenState.themeColor,
        isCut = screenState.isShapeCut == true,
    ) {

        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        var bottomSheetRoute by remember { mutableStateOf<Routes?>(null) }
        val sheetState = rememberModalBottomSheetState()

        val bottomSheetCornerRadius by animateDpAsState(
            targetValue = if (sheetState.targetValue == SheetValue.Expanded) 0.dp else MaterialTheme.rounding.extraLargeIncreased,
            label = "bottomSheetCornerRadius",
            animationSpec = MaterialTheme.motionScheme.slowSpatialSpec()
        )

        val navigationItems = if (screenState.authStatus is AuthStatus.Unauthorized){
            guestNavigationItems
        } else {
            authorizedNavigationItems
        }

        LaunchedEffect(Unit) {
            navigator.navigationActions.collect { action ->
                when (action) {
                    is NavigationAction.Navigate -> {
                        sheetState.hide()
                        navController.navigate(action.route)
                    }

                    is NavigationAction.NavigateAndClearBackStack -> {
                        navController.navigate(action.route) {
                            popUpTo(action.popupTo) {
                                inclusive = action.inclusive
                            }
                        }
                    }

                    NavigationAction.NavigateUp -> {
                        navController.navigateUp()
                    }

                    is NavigationAction.OpenAsBottomSheet -> {
                        bottomSheetRoute = action.route
                    }

                    is NavigationAction.HideBottomSheet -> {
                        sheetState.hide()
                        bottomSheetRoute = null
                    }

                }
            }
        }

        if (MaterialTheme.isTab){
            TabUI(
                navController = navController,
                currentDestination = currentDestination,
                navigationItems = navigationItems,
                authStatusNotify = screenState.authStatusNotify,
                onEvent = onEvent
            )
        }
        else {
            PhoneUi(
                navController = navController,
                currentDestination = currentDestination,
                navigationItems = navigationItems,
                authStatusNotify = screenState.authStatusNotify,
                onEvent = onEvent
            )
        }


        if (bottomSheetRoute != null) {
            ModalBottomSheet(
                onDismissRequest = { bottomSheetRoute = null },
                sheetState = sheetState,
                shape = MaterialTheme.cornerShape(topStart = bottomSheetCornerRadius.coerceAtLeast(0.dp), topEnd = bottomSheetCornerRadius.coerceAtLeast(0.dp))
            ) {
                when (val route = bottomSheetRoute) {
                    is Routes.SearchGroup -> SearchGroupRoute()
                    is Routes.ShareScore -> ShareScoreRoute()
                    is Routes.LessonDetail -> LessonDetailRoute(
                        name = route.name,
                        type = route.type,
                        aud = route.aud,
                        personIds = route.personIds,
                        startTime = route.getStartTime(),
                        endTime = route.getEndTime(),
                        date = route.getDate()
                    )
                    null -> {}
                    else -> {}
                }
            }
        }


    }




}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=673dp,height=841dp")
@Composable
fun MainScreenPreview() {
    AppTheme {
        MainScreen(
            navigator = object : Navigator {
                override val navigationActions = emptyFlow<NavigationAction>()
                override fun navigate(route: Routes) {}
                override fun navigateUp() {}
                override fun navigateAndClearBackStack(
                    route: Routes,
                    popupTo: Routes,
                    inclusive: Boolean
                ) {}
                override fun openAsBottomSheet(route: Routes) {}
                override fun hideBottomSheet() {}
            },
            screenState = MainScreenState(AuthStatus.Unauthorized, null, null, null, null),
            onEvent = {}
        )
    }
}
