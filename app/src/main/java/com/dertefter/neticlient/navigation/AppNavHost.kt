package com.dertefter.neticlient.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.dertefter.auth.AuthRoute
import com.dertefter.calendar.CalendarRoute
import com.dertefter.contact_info.ContactInfoRoute
import com.dertefter.home.HomeRoute
import com.dertefter.image_viewer.ImageViewerRoute
import com.dertefter.lesson_detail.LessonDetailRoute
import com.dertefter.messages.MessagesRoute
import com.dertefter.messages_detail.MessagesDetailRoute
import com.dertefter.money.MoneyRoute
import com.dertefter.navigation.Routes
import com.dertefter.news_detail.NewsDetailRoute
import com.dertefter.person_detail.PersonDetailRoute
import com.dertefter.profile.ProfileRoute
import com.dertefter.search_group.SearchGroupRoute
import com.dertefter.search_person.SearchPersonRoute
import com.dertefter.sessia_results.SessiaResultsRoute
import com.dertefter.settings.SettingsRoute
import com.dertefter.settings_account.SettingsAccountRoute
import com.dertefter.settings_labs.SettingsLabsRoute
import com.dertefter.settings_notifications.SettingsNotificationsRoute
import com.dertefter.settings_theme.SettingsThemeRoute
import com.dertefter.share_score.ShareScoreRoute
import com.dertefter.swap_lks.SwapLksRoute

@SuppressLint("RestrictedApi")
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navItems: List<AppNavigationItem>
) {
    if (navItems.isEmpty()) return

    LaunchedEffect(navController) {
        navController.currentBackStack.collect { backStack ->
            val routes = backStack
                .mapNotNull { it.destination.route?.substringAfterLast(".") }
                .joinToString(" -> ")
        }
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Routes.Tab1
    ) {
        navItems.forEach { item ->
            navigation(
                startDestination = item.startDestination,
                route = item.tab::class
            ) {
                graph()
            }
        }
    }
}

@Composable
fun RouteContent(route: Routes) {
    when (route) {
        is Routes.Profile -> ProfileRoute()
        is Routes.Settings -> SettingsRoute()
        is Routes.ContactInfo -> ContactInfoRoute()
        is Routes.Home -> HomeRoute()
        is Routes.NewsDetail -> NewsDetailRoute(
            newsId = route.newsId,
            previewUrl = route.previewUrl,
            type = route.type,
            tags = route.tags,
            date = route.date,
            contentColor = route.contentColor
        )
        is Routes.Auth -> AuthRoute()
        is Routes.SearchGroup -> SearchGroupRoute()
        is Routes.Calendar -> CalendarRoute()
        is Routes.SettingsAccount -> SettingsAccountRoute()
        is Routes.SettingsTheme -> SettingsThemeRoute()
        is Routes.SettingsNotifications -> SettingsNotificationsRoute()
        is Routes.Messages -> MessagesRoute()
        is Routes.MessagesDetail -> MessagesDetailRoute(idMessage = route.idMessage, idStudent = route.idStudent )
        is Routes.PersonDetail -> PersonDetailRoute(personId = route.personId)
        is Routes.SearchPerson -> SearchPersonRoute()
        is Routes.LessonDetail -> LessonDetailRoute(
            name = route.name,
            type = route.type,
            aud = route.aud,
            personIds = route.personIds,
            startTime = route.getStartTime(),
            endTime = route.getEndTime(),
            date = route.getDate()
        )
        is Routes.SessiaResults -> SessiaResultsRoute()
        is Routes.SwapLks -> SwapLksRoute()
        is Routes.Money -> MoneyRoute()
        is Routes.ShareScore -> ShareScoreRoute()
        is Routes.SettingsLabs -> SettingsLabsRoute()
        is Routes.ImageViewer -> ImageViewerRoute(
            imageUrls = route.imageUrls,
            viewPosition = route.viewPosition
        )
        else -> {}
    }
}

fun NavGraphBuilder.graph() {
    composable<Routes.Profile> {
        RouteContent(Routes.Profile)
    }

    composable<Routes.Settings> {
        RouteContent(Routes.Settings)
    }

    composable<Routes.SettingsAccount> {
        val args = it.toRoute<Routes.SettingsAccount>()
        RouteContent(args)
    }

    composable<Routes.ContactInfo> {
        RouteContent(Routes.ContactInfo)
    }

    composable<Routes.Home> {
        RouteContent(Routes.Home)
    }

    composable<Routes.NewsDetail>(
        deepLinks = listOf(
            navDeepLink { uriPattern = "https://nstu.ru/news/news_more?idnews={newsId}" },
            navDeepLink { uriPattern = "https://www.nstu.ru/news/news_more?idnews={newsId}" }
        )
    ) {
        val args = it.toRoute<Routes.NewsDetail>()
        RouteContent(args)
    }

    composable<Routes.Auth> {
        RouteContent(Routes.Auth)
    }

    composable<Routes.SwapLks> {
        RouteContent(Routes.SwapLks)
    }

    composable<Routes.SearchGroup> {
        RouteContent(Routes.SearchGroup)
    }

    composable<Routes.Calendar> {
        RouteContent(Routes.Calendar)
    }

    composable<Routes.Money> {
        RouteContent(Routes.Money)
    }

    composable<Routes.Messages> {
        RouteContent(Routes.Messages)
    }

    composable<Routes.SearchPerson> {
        RouteContent(Routes.SearchPerson)
    }


    composable<Routes.MessagesDetail> {
        val args = it.toRoute<Routes.MessagesDetail>()
        RouteContent(args)
    }

    composable<Routes.PersonDetail> {
        val args = it.toRoute<Routes.PersonDetail>()
        RouteContent(args)
    }

    composable<Routes.LessonDetail> {
        val args = it.toRoute<Routes.LessonDetail>()
        RouteContent(args)
    }

    composable<Routes.SessiaResults> {
        RouteContent(Routes.SessiaResults)
    }

    composable<Routes.ShareScore> {
        RouteContent(Routes.ShareScore)
    }

    composable<Routes.SettingsLabs> {
        RouteContent(Routes.SettingsLabs)
    }

    composable<Routes.SettingsTheme> {
        RouteContent(Routes.SettingsTheme)
    }

    composable<Routes.SettingsNotifications> {
        RouteContent(Routes.SettingsNotifications)
    }

    composable<Routes.ImageViewer> {
        val args = it.toRoute<Routes.ImageViewer>()
        RouteContent(args)
    }

}
