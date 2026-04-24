package com.dertefter.data.repository

import com.dertefter.data.dto.user.ContactInfoDto
import kotlinx.coroutines.flow.Flow

interface ContactInfoRepository {

    fun getContactInfo(): Flow<ContactInfoDto?>

    suspend fun saveContactInfo(
        email: String,
        address: String,
        phone: String,
        snils: String,
        oms: String,
        vk: String,
        tg: String,
        leader: String,
    ): Result<ContactInfoDto>

    suspend fun updateContactInfo(): Result<ContactInfoDto>

}