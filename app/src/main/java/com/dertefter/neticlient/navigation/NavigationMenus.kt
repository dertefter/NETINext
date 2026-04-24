package com.dertefter.neticlient.navigation

import com.dertefter.design.icons.Icons
import com.dertefter.navigation.Routes

val authorizedNavigationItems = listOf(
    AppNavigationItem(
        label = "Home",
        startDestination = Routes.Home,
        icon = { Icons.Home },
        tab = Routes.Tab1
    ),

    AppNavigationItem(
        label = "Calendar",
        startDestination = Routes.Calendar,
        icon = { Icons.Calendar },
        tab = Routes.Tab2

    ),

    AppNavigationItem(
        label = "Messages",
        startDestination = Routes.Messages,
        icon = { Icons.Mail },
        tab = Routes.Tab3

    ),

    AppNavigationItem(
        label = "Profile",
        startDestination = Routes.Profile,
        icon = { Icons.User },
        tab = Routes.Tab4

    ),
)

val guestNavigationItems = listOf(
    AppNavigationItem(
        label = "Home",
        startDestination = Routes.Home,
        icon = { Icons.Home },
        tab = Routes.Tab1
    ),

    AppNavigationItem(
        label = "Calendar",
        startDestination = Routes.Calendar,
        icon = { Icons.Calendar },
        tab = Routes.Tab2

    ),

    AppNavigationItem(
        label = "Profile",
        startDestination = Routes.Profile,
        icon = { Icons.User },
        tab = Routes.Tab3
    ),
)
