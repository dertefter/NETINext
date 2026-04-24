package com.dertefter.sessia_results.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.cornerShape
import com.dertefter.design.theme.spacing

@Composable
fun ValueItem(
    modifier: Modifier = Modifier,
    title: String,
    value: String? = null
) {

    Row(
        modifier = modifier
            .clip(MaterialTheme.cornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .padding(horizontal = MaterialTheme.spacing.medium, vertical = MaterialTheme.spacing.small)
        )
        value?.let{ value ->
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelMediumEmphasized,
                modifier = Modifier
                    .clip(MaterialTheme.cornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = MaterialTheme.spacing.medium, vertical = MaterialTheme.spacing.small)
            )
        }

    }

}

@Preview(showBackground = false)
@Composable
fun ValueItemPreview2() {
    AppTheme {
        ValueItem(
            title = "Title",
            value = "Value"
        )
    }
}

@Preview(showBackground = false)
@Composable
fun ValueItemPreview() {
    AppTheme {
        ValueItem(
            title = "Title",
            value = "Value"
        )
    }
}