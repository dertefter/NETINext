package com.dertefter.design.components.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.design.R
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.circleShape
import com.dertefter.design.theme.spacing

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AuthRequestCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {


    Box {
        Column(
            modifier = modifier
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(MaterialTheme.spacing.large),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.design_auth_req_title),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.headlineMediumEmphasized,
                    fontWeight = FontWeight(600),
                    modifier = Modifier.weight(1f),
                )

                FilledIconButton(
                    modifier = Modifier.fillMaxHeight(), onClick = onClick,
                    shape = MaterialTheme.circleShape()
                ) {
                    Icon(
                        imageVector = Icons.ArrowForward, contentDescription = null
                    )
                }
            }


            Text(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                text = stringResource(R.string.design_auth_req_text),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }


}

@Preview(locale = "ru")
@Composable
private fun AuthRequestCardPreview() {
    AppTheme {
        AuthRequestCard()
    }
}
