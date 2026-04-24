package com.dertefter.person_detail.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing

@Composable
fun StringList(
    modifier: Modifier = Modifier,
    strings: List<String>,
    onStringClicked: (String) -> Unit = {},
){

    Column(
        modifier = modifier
        .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        for (s in strings){
            Text(
                text = AnnotatedString.fromHtml(htmlString = s),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .clickable(onClick = { onStringClicked(s) })
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(
                        vertical = MaterialTheme.spacing.large,
                        horizontal = MaterialTheme.spacing.extraLarge
                    )
                    .fillMaxWidth()
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun StringListPreview() {
    AppTheme {
        StringList(
            strings = listOf("String 1", "String 2", "String 3")
        )
    }
}

