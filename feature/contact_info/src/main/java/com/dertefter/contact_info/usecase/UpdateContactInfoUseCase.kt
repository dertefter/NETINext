package com.dertefter.contact_info.usecase

import com.dertefter.data.dto.user.ContactInfoDto
import com.dertefter.data.repository.ContactInfoRepository
import javax.inject.Inject

class UpdateContactInfoUseCase @Inject constructor(
    private val contactInfoRepository: ContactInfoRepository
) {
    suspend operator fun invoke(): Result<ContactInfoDto> {
        return contactInfoRepository.updateContactInfo()
    }
}
