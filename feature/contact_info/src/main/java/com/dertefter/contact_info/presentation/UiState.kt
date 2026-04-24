package com.dertefter.contact_info.presentation

import com.dertefter.contact_info.domain.ContactInfo
import com.dertefter.contact_info.domain.UserInfo
import com.dertefter.data.common.AppError

data class UiState(
    val contactInfo: ContactInfo? = null,
    val isLoading: Boolean = false,
    val error: AppError? = null,
    val validateFails: List<ValidateFail> = emptyList(),
)