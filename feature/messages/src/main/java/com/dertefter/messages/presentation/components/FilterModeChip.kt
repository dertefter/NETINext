package com.dertefter.messages.presentation.components

import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dertefter.messages.R
import com.dertefter.messages.presentation.FilterMode

@Composable
fun FilterModeChip(
    modifier: Modifier = Modifier,
    selected: Boolean,
    filterMode: FilterMode,
    onClick: () -> Unit,

    ) {
    InputChip(
        selected = selected,
        onClick =onClick,
        label = {
            Text(
                text = when (filterMode){
                    FilterMode.ALL -> stringResource(R.string.all)
                    FilterMode.DELETED -> stringResource(R.string.archive)
                    is FilterMode.TAB -> when (filterMode.tabId) {
                        0 -> stringResource(R.string.other)
                        1 -> stringResource(R.string.decan)
                        2 -> stringResource(R.string.teachers)
                        3 -> stringResource(R.string.services)
                        101 -> stringResource(R.string.teacher_and_services)
                        else -> stringResource(R.string.other)
                    }
                    FilterMode.UNREAD -> stringResource(R.string.unread)
                }
            )
        },
        modifier = modifier,

    )
}
