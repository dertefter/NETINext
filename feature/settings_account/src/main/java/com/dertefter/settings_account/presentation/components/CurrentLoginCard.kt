package com.dertefter.settings_account.presentation.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import com.dertefter.design.theme.spacing

@Composable
fun CurrentLoginCard(
    modifier: Modifier = Modifier,
    login: String,
    onLogOut: () -> Unit = {}
){
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxWidth()
    ) {
        Text(
            text = login,
            modifier = Modifier
                .padding(top = MaterialTheme.spacing.large)
                .padding(horizontal = MaterialTheme.spacing.large)
        )

        Text(
            text = "Выйти",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .align(Alignment.End)
                .clip(MaterialTheme.shapes.large)
                .clickable(onClick = onLogOut)
                .padding(MaterialTheme.spacing.large)
        )
    }
}

@Composable
@Preview
fun CurrentLoginCardPreview(){
    AppTheme() {
        CurrentLoginCard(
            login = "kjhdjkdvjvdjkhdv"
        )
    }

}
