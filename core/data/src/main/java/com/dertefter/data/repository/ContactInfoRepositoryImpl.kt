package com.dertefter.data.repository

import com.dertefter.data.datasource.local.LocalDataSource
import com.dertefter.data.datasource.remote.RemoteDataSource
import com.dertefter.data.dto.user.ContactInfoDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContactInfoRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : ContactInfoRepository {


    override fun getContactInfo(): Flow<ContactInfoDto?> {
        return localDataSource.getContactInfo()
    }

    override suspend fun saveContactInfo(
        email: String,
        address: String,
        phone: String,
        snils: String,
        oms: String,
        vk: String,
        tg: String,
        leader: String,
    ): Result<ContactInfoDto> {
        return remoteDataSource.saveContactInfo(
            email = email,
            address = address,
            phone = phone,
            snils = snils,
            oms = oms,
            vk = vk,
            tg = tg,
            leader = leader
        ).onSuccess {
            localDataSource.saveContactInfo(it)
        }
    }


    override suspend fun updateContactInfo(): Result<ContactInfoDto> {
        return remoteDataSource.getContactInfo().onSuccess {
            localDataSource.saveContactInfo(it)
        }
    }

}