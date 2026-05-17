package com.dertefter.docs.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.cornerShape
import com.dertefter.design.theme.rounding
import com.dertefter.design.theme.spacing

@Composable
fun DocsItem(
    modifier: Modifier = Modifier,
    type: String,
    status: String? = null,
    number: String? = null,
    onClick: () -> Unit = {},
    isFirst: Boolean = false,
    isLast: Boolean = false
){


    val topRounding = if (isFirst) MaterialTheme.rounding.largeIncreased else MaterialTheme.rounding.small

    val bottomRounding  = if (isLast) MaterialTheme.rounding.largeIncreased else MaterialTheme.rounding.small

    val shape = MaterialTheme.cornerShape(
        topStart = topRounding,
        topEnd = topRounding,
        bottomStart = bottomRounding,
        bottomEnd = bottomRounding
    )


    Column(
        modifier = modifier
            .clip(shape)
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(MaterialTheme.spacing.extraLarge)
            .fillMaxWidth()
        ,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = type,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            number?.let { number ->
                Text(
                    text = number,
                    style = MaterialTheme.typography.labelLargeEmphasized,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }

        status?.let { status ->
            Text(
                text = status,
                style = MaterialTheme.typography.labelLargeEmphasized,
            )
        }

    }

}

@Preview(showBackground = false)
@Composable
private fun DocsItemPreview() {
    AppTheme {
        DocsItem(
            type = "ddddddd",
            status = "1dddd",
            number = "111111"
        )
    }
}
