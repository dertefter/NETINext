package com.dertefter.new_document.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.data.dto.docs.DocumentOptionItem
import com.dertefter.data.dto.docs.DocumentRequestItem
import com.dertefter.design.components.appbar.AppToolbar
import com.dertefter.design.components.buttons.AppNavigationIcon
import com.dertefter.design.components.common.ErrorLarge
import com.dertefter.design.components.loading.AppLoadingIndicator
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.circleShape
import com.dertefter.design.theme.spacing
import com.dertefter.new_document.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NewDocumentScreen(
    onEvent: (Event) -> Unit,
    uiState: UiState,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    LaunchedEffect(uiState.optionList) {
        if (uiState.optionList.isNotEmpty()){
            onEvent(
                Event.OnSelectOption(uiState.optionList.first())
            )
        }

    }

    Scaffold(
        topBar = {
            AppToolbar(
                title = stringResource(R.string.new_document_title),
                navigationIcon = {
                    AppNavigationIcon(
                        onClick = {
                            onEvent(Event.OnNavigateUp)
                        }
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { contentPadding ->

        if (uiState.isError){
            Box(
                modifier = Modifier
                    .padding(bottom = MaterialTheme.spacing.defaultScreenPadding)
                    .padding(MaterialTheme.spacing.defaultScreenPadding)
                    .padding(contentPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                ErrorLarge(
                    onRetry = {
                        onEvent(
                            Event.OnUpdateOptions
                        )
                    }
                )
            }

        } else if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .padding(bottom = MaterialTheme.spacing.defaultScreenPadding)
                    .padding(MaterialTheme.spacing.defaultScreenPadding)
                    .padding(contentPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                AppLoadingIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            )
            {
                var expanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth(),
                )
                {
                    OutlinedTextField(
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                            .fillMaxWidth(),
                        value = uiState.selectedOption?.text ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.new_document_select_type_label)) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        shape = MaterialTheme.shapes.large,
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        uiState.optionList.forEach { option ->
                            val isSelected = option == uiState.selectedOption
                            DropdownMenuItem(
                                modifier = if (isSelected) {
                                    Modifier
                                        .padding(horizontal = 8.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            shape = MaterialTheme.shapes.medium
                                        )
                                } else {
                                    Modifier
                                },
                                text = { Text(text = option.text) },
                                onClick = {
                                    onEvent(Event.OnSelectOption(option))
                                    expanded = false
                                },
                                leadingIcon = if (isSelected) {
                                    {
                                        Icon(
                                            imageVector = Icons.Check,
                                            contentDescription = null
                                        )
                                    }
                                } else null,
                                colors = if (isSelected) {
                                    MenuDefaults.itemColors(
                                        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                        leadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                } else {
                                    MenuDefaults.itemColors()
                                }
                            )
                        }
                    }
                }

                uiState.documentRequest?.let { documentRequest ->

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = uiState.comment,
                            onValueChange = {
                                onEvent(
                                    Event.OnCommentChanged(it)
                                )
                            },
                            label = {
                                Text(stringResource(R.string.new_document_comment_label))
                            },
                            supportingText = {
                                Text(documentRequest.text_comm ?: "")
                            },
                            shape = MaterialTheme.shapes.large,
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        )

                        if (!documentRequest.text_doc.isNullOrEmpty()){
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Info,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(vertical = MaterialTheme.spacing.extraSmall)
                                        .size(20.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = documentRequest.text_doc ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Button(
                            onClick = {
                                onEvent(
                                    Event.OnConfirmClaim
                                )
                            },
                            modifier = Modifier
                                .align(Alignment.End),
                            shape = MaterialTheme.circleShape(),
                            enabled = documentRequest.is_avail == "1"
                        ){
                            Text(
                                stringResource(R.string.new_document_create_button)
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
private fun NewDocumentScreenPreview() {
    AppTheme {

        val u1 = UiState(
            optionList = listOf(
                DocumentOptionItem("Справка об обучении", "1"),
                DocumentOptionItem("Зачетная книжка", "2"),
                DocumentOptionItem("Диплом", "3")
            ),
            documentRequest = DocumentRequestItem(
                is_avail = "",
                need_appl = "",
                need_pay = "",
                need_verify = "",
                text_comm = "ваши комм",
                text_doc = ""
            ),
            selectedOption = DocumentOptionItem("Справка об обучении", "1"),
        )

        NewDocumentScreen(
            onEvent = {},
            uiState = u1
        )
    }
}

