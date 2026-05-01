package com.dertefter.contact_info.usecase

import com.dertefter.data.dto.user.ContactInfoDto
import com.dertefter.data.repository.ContactInfoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetContactInfoFlowUseCase @Inject constructor(
    private val contactInfoRepository: ContactInfoRepository
) {
    operator fun invoke(): Flow<ContactInfoDto?> {
        return contactInfoRepository.getContactInfo()
    }
}
