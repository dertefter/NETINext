package com.dertefter.home.presentation.content

import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dertefter.design.components.appbar.AppToolbar
import com.dertefter.design.components.appbar.Headline

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsHeader(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
){
    AppToolbar(
        title = "Жизнь университета",
        modifier = modifier,
        scrollBehavior = scrollBehavior,
    )
}