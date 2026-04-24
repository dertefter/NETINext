package com.dertefter.money.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing

@Composable
fun MoneyItem(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
){

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.spacing.small)
        ,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Text(
            text = title.capitalize(LocalLocale.current),
            style = MaterialTheme.typography.titleLargeEmphasized,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun MoneyItemPreview() {
    AppTheme {
        MoneyItem(
            title = "Стипендия",
            value = "10 000 руб."
        )
    }
}
