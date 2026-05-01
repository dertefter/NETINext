package com.dertefter.doc_detail.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.circleShape
import com.dertefter.doc_detail.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DocDetailScreen(
    type: String,
    date: String?,
    status: String?,
    person: String?,
    comment: String?,
    number: String?,
    uiState: UiState,
    onEvent: (Event) -> Unit,
) {

    Scaffold(
      containerColor = Color.Transparent
    ) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding,
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.defaultScreenPadding),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            horizontalAlignment = Alignment.End
        ) {
            item {
                Text(
                    modifier = Modifier
                        .padding(
                            all = MaterialTheme.spacing.small
                        )
                        .fillMaxWidth(),
                    text = type,
                    style = MaterialTheme.typography.headlineMedium,
                )
            }



            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.largeIncreased)
                        .fillMaxWidth()
                ){
                    if (!date.isNullOrEmpty()) {
                        DetailItem(
                            text = date,
                            icon = Icons.Calendar
                        )
                    }

                    if (!person.isNullOrEmpty()) {
                        DetailItem(
                            text = person,
                            icon = Icons.User
                        )
                    }

                    if (!comment.isNullOrEmpty()) {
                        DetailItem(
                            text = comment,
                            icon = Icons.Subject
                        )
                    }

                    if (!status.isNullOrEmpty()) {
                        DetailItem(
                            text = status,
                            colorize = true
                        )
                    }

                }
            }

            number?.let{ number ->
                if (uiState.cancelable){
                    item {
                        Button(
                            shape = MaterialTheme.circleShape(),
                            onClick = {
                                onEvent(
                                    Event.OnCancelClaim(
                                        docNumber = number
                                    )
                                )
                            },
                            enabled = !uiState.isLoading
                        ){
                            Text(
                                text = stringResource(R.string.docs_detail_delete)
                            )
                        }
                    }
                }

            }




        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DocDetailScreenPreview() {
    AppTheme {
        DocDetailScreen(
            type = "Справка об обучении",
            date = "20.10.2023",
            status = "Готово",
            person = "Иванов Иван Иванович",
            comment = "fffffff",
            number = "123456",
            uiState = UiState(),
            onEvent = {}
        )
    }
}



