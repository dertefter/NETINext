package com.dertefter.contact_info.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.contact_info.R
import com.dertefter.contact_info.model.toContactInfo
import com.dertefter.design.components.text_fields.TextFieldItem
import com.dertefter.data.dto.user.ContactInfoDto
import com.dertefter.design.components.PullToRefreshIndicator
import com.dertefter.design.components.appbar.AppToolbar
import com.dertefter.design.components.buttons.AppNavigationIcon
import com.dertefter.design.components.common.ErrorCard
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.rounding
import com.dertefter.design.theme.spacing

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ContactInfoScreen(
    onEvent: (Event) -> Unit,
    uiState: UiState,
    savingState: SavingState,
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())


    Scaffold(
        topBar = {
            AppToolbar(
                title = stringResource(R.string.contact_info_title),
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    AppNavigationIcon(
                        onClick = {
                            onEvent(Event.OnNavigateUp)
                        })
                },
            )
    }, floatingActionButton = {
        AnimatedVisibility(savingState.isSaveEnabled) {
            ExtendedFloatingActionButton(
                modifier = Modifier,
                onClick = {
                onEvent(Event.OnSave)
            },
                icon = {
                Crossfade(savingState.isLoading) { isLoading ->
                    if (isLoading) {
                        LoadingIndicator()
                    } else {
                        Icon(Icons.Save, null)
                    }
                }


            },
                text = { Text(stringResource(R.string.contact_info_save)) },
                expanded = (!savingState.isLoading)
            )
        }
    }) { contentPadding ->

        val state = rememberPullToRefreshState()

        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            state = state,
            isRefreshing = uiState.isLoading,
            onRefresh = { onEvent(Event.OnRequestUpdate) },
            indicator = {
                PullToRefreshIndicator(
                    modifier = Modifier
                        .padding(contentPadding)
                        .align(Alignment.TopCenter),
                    state = state,
                    isRefreshing = uiState.isLoading
                )
            }) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {

                uiState.error?.let {
                    item {
                        ErrorCard(
                            title = stringResource(R.string.contact_info_update_error),
                            message = null,
                            onRetry = { onEvent(Event.OnRequestUpdate) })
                    }

                }


                uiState.contactInfo?.let { contactInfo ->

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = MaterialTheme.spacing.small),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        )
                        {

                            Text(
                                modifier = Modifier
                                    .padding(
                                        vertical = MaterialTheme.spacing.medium,
                                    )
                                    .padding(horizontal = MaterialTheme.rounding.large)
                                    .fillMaxWidth(),
                                text = stringResource(R.string.contact_info_section_general),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMediumEmphasized,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,

                                )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(MaterialTheme.shapes.largeIncreased),
                                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                            ){

                                TextFieldItem(
                                    value = contactInfo.email,
                                    onValueChange = {
                                        onEvent(Event.OnEmailChange(it))
                                    },
                                    hint = stringResource(R.string.contact_info_email_hint),
                                    icon = Icons.Mail,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                                )

                                TextFieldItem(
                                    value = contactInfo.mobilePhoneNumber,
                                    onValueChange = {
                                        onEvent(Event.OnMobilePhoneNumberChange(it))
                                    },
                                    hint = stringResource(R.string.contact_info_phone_hint),
                                    icon = Icons.Call,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    maxSymbols = 17,
                                    isError = uiState.validateFails.contains(ValidateFail.PHONE)
                                )

                                TextFieldItem(
                                    value = contactInfo.address,
                                    onValueChange = {
                                        onEvent(Event.OnAdressChange(it))
                                    },
                                    hint = stringResource(R.string.contact_info_address_hint),
                                    icon = Icons.Home,
                                )

                                TextFieldItem(
                                    value = contactInfo.snils,
                                    onValueChange = {
                                        onEvent(Event.OnSnilsChange(it))
                                    },
                                    hint = stringResource(R.string.contact_info_snils_hint),
                                    icon = Icons.Subtitles,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    maxSymbols = 14,
                                    isError = uiState.validateFails.contains(ValidateFail.SNILS)
                                )

                                TextFieldItem(
                                    value = contactInfo.oms,
                                    onValueChange = {
                                        onEvent(Event.OnOmsChange(it))
                                    },
                                    hint = stringResource(R.string.contact_info_oms_hint),
                                    icon = Icons.HealthCross,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )

                            }



                            Spacer(
                                modifier = Modifier.height(MaterialTheme.spacing.medium)
                            )

                            Text(
                                modifier = Modifier
                                    .padding(
                                        vertical = MaterialTheme.spacing.medium,
                                    )
                                    .padding(horizontal = MaterialTheme.rounding.large)
                                    .fillMaxWidth(),
                                text = stringResource(R.string.contact_info_section_social),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMediumEmphasized,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,

                                )

                            Column(
                                modifier = Modifier
                                    .padding(bottom = MaterialTheme.spacing.defaultScreenPadding)
                                    .fillMaxWidth()
                                    .clip(MaterialTheme.shapes.largeIncreased),
                                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                            )
                            {

                                TextFieldItem(
                                    value = contactInfo.vk,
                                    onValueChange = {
                                        onEvent(Event.OnVkChange(it))
                                    },
                                    hint = stringResource(R.string.contact_info_vk_hint),
                                    icon = Icons.VK,
                                )

                                TextFieldItem(
                                    value = contactInfo.telegram,
                                    onValueChange = {
                                        onEvent(Event.OnTelegramChange(it))
                                    },
                                    hint = stringResource(R.string.contact_info_telegram_hint),
                                    icon = Icons.TG,
                                )

                                TextFieldItem(
                                    value = contactInfo.leaderId,
                                    onValueChange = {
                                        onEvent(Event.OnLeaderIdChange(it))
                                    },
                                    hint = stringResource(R.string.contact_info_leaderid_hint),
                                    icon = Icons.LeaderID,
                                )

                            }
                        }
                    }
                }

            }


        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ContactInfoScreenPreview() {
    AppTheme {
        ContactInfoScreen(
             onEvent = {}, uiState = UiState(
                contactInfo = ContactInfoDto(
                    name = "Иван",
                    surname = "Иванов",
                    patronymic = "Иванович",
                    symGroup = "АБВ-123",
                    email = "ivanov@example.com",
                    address = "г. Новосибирск, ул. Пушкина, д. 10",
                    mobilePhoneNumber = "+7 999 123 45 67",
                    snils = "123-456-789 01",
                    oms = "1234567890123456",
                    vk = "https://vk.com/id1",
                    telegram = "@ivanov",
                    leaderId = "12345",
                ).toContactInfo(), isLoading = false
            ), savingState = SavingState(isLoading = true, isSaveEnabled = true)
        )
    }
}
