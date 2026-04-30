package com.dertefter.profile.presentation.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.spacing
import com.dertefter.navigation.Routes
import com.dertefter.profile.R
import com.dertefter.profile.presentation.components.RouteItem

@Composable
fun RoutesListMenu(
    modifier: Modifier = Modifier,
    routesList: List<Routes>,
    onRouteClick: (Routes) -> Unit = {}
){
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        for (route in routesList){
            val icon = when (route){
                Routes.SearchPerson -> Icons.PersonSearch
                Routes.SessiaResults -> Icons.School
                Routes.Money -> Icons.Payments
                Routes.Docs -> Icons.Docs
                else -> Icons.ArrowDropUp
            }
            val text = when (route){
                Routes.SearchPerson -> stringResource(R.string.person_search)
                Routes.SessiaResults -> stringResource(R.string.sessia_results)
                Routes.Money -> stringResource(R.string.money)
                Routes.Docs -> "Документы"
                else -> ""
            }

            RouteItem(
                title = text,
                icon = icon,
                onClick = {
                    onRouteClick(route)
                }
            )

        }


    }
}