package com.dertefter.design.components.schedule

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.design.components.loading.AppLoadingIndicator
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.circleShape
import com.dertefter.design.theme.spacing

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GroupButton(
    modifier: Modifier = Modifier,
    group: String? = null,
    isLoading: Boolean = false,
    isIndividual: Boolean = false,
    onClick: () -> Unit = {}
) {

    val icon = if (group == null) {
        Icons.AddGroup
    } else {
        Icons.Group
    }

    Row(
        modifier = modifier
            .height(40.dp)
            .clip(MaterialTheme.circleShape())
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable(onClick = onClick)
            .animateContentSize(
                animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        AnimatedContent(
            targetState = isLoading,
            label = "GroupButtonContent",
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            }
        ) { loading ->
            if (loading) {
                AppLoadingIndicator(
                    modifier = Modifier.size(40.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.large),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )

                    group?.let {
                        Text(
                            text = it.uppercase(),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun GroupButtonPreview1() {
    AppTheme(isCut = true) {
        GroupButton(
            group = "ПММ-51",
            isLoading = true
        )
    }
}


@Preview
@Composable
fun GroupButtonPreview2() {
    MaterialTheme {
        GroupButton(
            group = "jfjfjf-51",
            isLoading = false
        )
    }
}

@Preview
@Composable
fun GroupButtonPreview3() {
    MaterialTheme {
        GroupButton(
            isLoading = false
        )
    }
}
