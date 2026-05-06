package com.dertefter.search_group.presentation.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing
import com.dertefter.search_group.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GroupHistoryItem(
    modifier: Modifier = Modifier,
    group: GroupDto,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {

    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.secondary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = MaterialTheme.spacing.extraLarge),
            contentAlignment = Alignment.Center
        ) {
            Text(
                color = MaterialTheme.colorScheme.onSecondary,
                textAlign = TextAlign.Center,
                text = group.name.uppercase(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable(onClick = onClick)
            )
        }

        IconButton(
            onClick = onRemove,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Close,
                contentDescription = stringResource(R.string.search_group_remove_from_history),
                tint = MaterialTheme.colorScheme.onSecondary,
            )
        }

    }


}

@Preview(showBackground = true)
@Composable
fun GroupHistoryItemPreview() {
    AppTheme {

        val list = listOf(
            GroupDto(
                name = "титл",
                false
            ),
            GroupDto(
                name = "титл",
                true
            )

        )

        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            list.forEach {
                GroupHistoryItem(modifier = Modifier, group = it, {}, {})
            }
        }
    }
}
