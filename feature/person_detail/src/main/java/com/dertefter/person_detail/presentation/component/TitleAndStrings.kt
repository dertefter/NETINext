package com.dertefter.person_detail.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.rounding
import com.dertefter.design.theme.spacing

@Composable
fun TitleAndStrings(
    modifier: Modifier = Modifier,
    title: String,
    strings: List<String>,
    onStringClicked: (String) -> Unit = {},
){
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(horizontal = MaterialTheme.rounding.large,
                    vertical = MaterialTheme.spacing.medium),
            style = MaterialTheme.typography.titleMediumEmphasized
        )

        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
        ){
            StringList(
                strings = strings,
                onStringClicked = onStringClicked,

            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun TitleAndStringsPreview() {
    AppTheme {
        TitleAndStrings(
            title = "тестс",
            strings = listOf("String 1", "String 2", "String 3")
        )
    }
}
