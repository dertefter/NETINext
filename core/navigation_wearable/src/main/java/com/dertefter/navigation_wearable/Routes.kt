package com.dertefter.navigation_wearable

import kotlinx.serialization.Serializable

@Serializable
sealed interface Routes {

    @Serializable
    data object Home : Routes

    @Serializable
    data object Calendar : Routes

}
