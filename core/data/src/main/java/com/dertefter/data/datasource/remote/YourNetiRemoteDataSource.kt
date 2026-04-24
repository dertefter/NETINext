package com.dertefter.data.datasource.remote

import com.dertefter.data.dto.messsages.MessageDto
import com.dertefter.data.dto.sessia_results.SessiaResultDto
import com.dertefter.data.dto.user.ContactInfoDto
import com.dertefter.data.dto.user.UserInfoDto


interface YourNetiRemoteDataSource {

    suspend fun authorizeYourNeti(login: String, password: String): Result<Unit>

    fun logout()

    suspend fun getUserInfo(): Result<UserInfoDto>

    suspend fun getMessages(): Result<List<MessageDto>>

    suspend fun readMessage(idStudent: Long, idMessage: Long): Result<Unit>

    suspend fun unreadMessage(idStudent: Long, idMessage: Long): Result<Unit>

    suspend fun moveMessageToTrash(idStudent: Long, idMessage: Long): Result<Unit>

    suspend fun removeMessageFromTrash(idStudent: Long, idMessage: Long): Result<Unit>

    suspend fun getMessageById(idStudent:  Long, messageId: Long): Result<MessageDto>

    suspend fun getContactInfo(): Result<ContactInfoDto>

    suspend fun getSessiaResults(): Result<List<SessiaResultDto>>

}
