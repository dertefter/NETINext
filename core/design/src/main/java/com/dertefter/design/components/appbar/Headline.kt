package com.dertefter.design.components.appbar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.design.theme.spacing

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Headline(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = {},
){
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium,
        modifier = modifier
            .padding(vertical = MaterialTheme.spacing.small)
            .fillMaxWidth()

    )
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
fun HeadlinePreview(){
    MaterialTheme(){
        Headline(
            text = "Абоба",
        )
    }
}