package com.dertefter.design.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.IndicatorBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.IndicatorMaxDistance
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dertefter.design.components.loading.AppLoadingIndicator
import com.dertefter.design.theme.AppTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PullToRefreshIndicator(
    state: PullToRefreshState,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    indicatorColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    maxDistance: Dp = IndicatorMaxDistance,
) {

    IndicatorBox(
        modifier = modifier.size(48.dp),
        state = state,
        isRefreshing = isRefreshing,
        containerColor = containerColor,
        maxDistance = maxDistance,
    )
    {
        if (isRefreshing){
            AppLoadingIndicator(
                color = indicatorColor,
            )
        }else{
            AppLoadingIndicator(
                color = indicatorColor,
                progress = { state.distanceFraction / 2f },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview(showBackground = true)
@Composable
private fun PullToRefreshIndicatorPreview() {
    AppTheme {
        Box(
            modifier = Modifier.size(100.dp),
            contentAlignment = Alignment.Center
        ) {
            PullToRefreshIndicator(
                modifier = Modifier,
                state =  PullToRefreshState(),
                isRefreshing = true,

            )
        }
    }
}


