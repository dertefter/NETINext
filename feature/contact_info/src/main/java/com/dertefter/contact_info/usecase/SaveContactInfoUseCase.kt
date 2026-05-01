package com.dertefter.contact_info.usecase

import com.dertefter.data.dto.user.ContactInfoDto
import com.dertefter.data.repository.ContactInfoRepository
import javax.inject.Inject

class SaveContactInfoUseCase @Inject constructor(
    private val contactInfoRepository: ContactInfoRepository
) {
    suspend operator fun invoke(
        email: String,
        address: String,
        phone: String,
        snils: String,
        oms: String,
        vk: String,
        tg: String,
        leader: String,
    ): Result<ContactInfoDto> {
        return contactInfoRepository.saveContactInfo(
            email = email,
            address = address,
            phone = phone,
            snils = snils,
            oms = oms,
            vk = vk,
            tg = tg,
            leader = leader
        )
    }
}
