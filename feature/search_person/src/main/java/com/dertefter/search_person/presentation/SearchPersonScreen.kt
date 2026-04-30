package com.dertefter.search_person.presentation

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.design.components.buttons.AppNavigationIcon
import com.dertefter.design.components.common.ErrorLarge
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.circleShape
import com.dertefter.design.theme.spacing
import com.dertefter.search_person.R
import com.dertefter.search_person.presentation.componets.PersonItem
import java.util.Locale

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchPersonScreen(
    onEvent: (Event) -> Unit,
    uiState: UiState,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val colors = TopAppBarDefaults.topAppBarColors()
    val containerColor by animateColorAsState(
        targetValue = if (scrollBehavior.state.contentOffset < 0f) {
            colors.scrolledContainerColor
        } else {
            colors.containerColor
        },
        label = "TopBarColor"
    )

    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText: String? =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            spokenText?.let {
                onEvent(Event.OnSearchQueryChanged(it))
            }
        }
    }

    Scaffold(
        topBar = {
            Surface(
                color = containerColor
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = MaterialTheme.spacing.medium)
                        .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding)
                        .padding(bottom = MaterialTheme.spacing.medium)
                        .displayCutoutPadding()
                        .statusBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ){

                AppNavigationIcon(
                    onClick = {
                        onEvent(Event.OnNavigateBack)
                    }
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = uiState.query,
                    onValueChange = { onEvent(Event.OnSearchQueryChanged(it)) },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search_person_search)
                        ) },
                    leadingIcon = { Icon(Icons.Search, contentDescription = stringResource(R.string.search_person_search)) },
                    trailingIcon = {
                        if (uiState.query.isNotEmpty()) {
                            IconButton(
                                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small),
                                onClick = {
                                    onEvent(Event.OnSearchQueryChanged(""))
                                }
                            ) {
                                Icon(Icons.Close, contentDescription = stringResource(R.string.search_person_clear))
                            }
                        } else {
                            IconButton(
                                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small),
                                onClick = {
                                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                                        putExtra(RecognizerIntent.EXTRA_PROMPT, "Произнесите название группы")
                                    }
                                    try{
                                        speechRecognizerLauncher.launch(intent)
                                    } catch (_: Exception){}

                                }
                            ) {
                                Icon(Icons.Mic, contentDescription = stringResource(R.string.search_person_voice_recogintion))
                            }
                        }
                    },
                    singleLine = true,
                    shape = MaterialTheme.circleShape(),
                    isError = uiState.error != null,
                    colors = OutlinedTextFieldDefaults.colors().copy(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                    ),
                )
            }
        }
    }
    ) { contentPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (uiState.persons.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                        .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding)
                        .imePadding()
                ) {
                    if (uiState.isLoading) {
                        LoadingIndicator(
                            Modifier.align(Alignment.Center)
                        )
                    } else {
                        if (uiState.error != null) {
                            ErrorLarge()
                        } else {
                            val text = if (uiState.query.isEmpty()){
                                stringResource(R.string.search_person_hint)
                            } else {
                                stringResource(R.string.search_person_nothing_searched)
                            }
                            Text(
                                text = text,
                                modifier = Modifier.align(Alignment.Center),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }

            LazyColumn(
                contentPadding = contentPadding,
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding),
            ) {
                items(uiState.persons) { person ->
                    PersonItem(
                        name = person.name,
                        avatarUrl = person.avatarUrl,
                        onClick = {
                            onEvent(
                                Event.OnOpenPerson(person.personId)
                            )
                        },
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SrarchPersonScreenPreview(){
    AppTheme(
        isCut = true
    ) {
        SearchPersonScreen(
            onEvent = {},
            uiState = UiState(query = "", emptyList()),

        )
    }
}