package com.dertefter.data.datasource.remote

import com.dertefter.data.datasource.remote.api.YourNetiApiService
import com.dertefter.data.dto.auth.AuthYourNetiRequest
import com.dertefter.data.dto.messsages.MessageDto
import com.dertefter.data.dto.messsages.MoveMessageToTrashRequest
import com.dertefter.data.dto.messsages.ReadMessageRequest
import com.dertefter.data.dto.messsages.RemoveMessageFromTrashRequest
import com.dertefter.data.dto.messsages.UnreadMessageRequest
import com.dertefter.data.dto.sessia_results.SessiaResultDto
import com.dertefter.data.dto.user.ContactInfoDto
import com.dertefter.data.dto.user.UserInfoDto
import javax.inject.Inject
import javax.inject.Named

class YourNetiRemoteDataSourceImpl @Inject constructor(
    private val yourNetiApiService: YourNetiApiService,
    @param:Named("your_neti") private val  yourNetiSessionCookieJar: SessionCookieJar,
) : YourNetiRemoteDataSource {

    companion object {
        const val TAG = "YourNetiRemoteDataSourceImpl"
    }


    override suspend fun authorizeYourNeti(login: String, password: String): Result<Unit> {
        return runCatching {
            yourNetiSessionCookieJar.cleanUp()
            val authorized = yourNetiApiService.newAuth(body = AuthYourNetiRequest(login, password))
            if (!authorized.login){
                throw NullPointerException()
            }
        }

    }

    override fun logout() {
        yourNetiSessionCookieJar.cleanUp()
    }


    override suspend fun getUserInfo(): Result<UserInfoDto> {
        return runCatching {
            yourNetiApiService.getUserInfo().first()
        }
    }

    override suspend fun getContactInfo(): Result<ContactInfoDto> {
        return runCatching {
            yourNetiApiService.getUserInfo().first().toContactInfoDto()
        }
    }

    override suspend fun getSessiaResults(): Result<List<SessiaResultDto>> {
        return runCatching {
            yourNetiApiService.getSessiaResults()
        }
    }


    override suspend fun getMessages(): Result<List<MessageDto>> {
        return runCatching {
            yourNetiApiService.getMessages()
        }
    }

    override suspend fun readMessage(
        idStudent:  Long, idMessage:  Long
    ): Result<Unit> {
        return runCatching {
            yourNetiApiService.readMessage(ReadMessageRequest(idStudent = idStudent, idMessage = idMessage))
        }
    }

    override suspend fun unreadMessage(
        idStudent:  Long, idMessage:  Long
    ): Result<Unit> {
        return runCatching {
            yourNetiApiService.unreadMessage(UnreadMessageRequest(idStudent = idStudent, idMessage = idMessage))
        }
    }

    override suspend fun moveMessageToTrash(
        idStudent:  Long,
        idMessage:  Long
    ): Result<Unit> {
        return runCatching {
            yourNetiApiService.moveMessageToTrash(MoveMessageToTrashRequest(idStudent = idStudent, idMessage = idMessage))
        }
    }

    override suspend fun removeMessageFromTrash(
        idStudent:  Long,
        idMessage:  Long
    ): Result<Unit> {
        return runCatching {
            yourNetiApiService.removeMessageFromTrash(RemoveMessageFromTrashRequest(idStudent = idStudent, idMessage = idMessage))
        }
    }

    override suspend fun getMessageById(idStudent:  Long, messageId: Long): Result<MessageDto> {
        return runCatching {
            readMessage(idStudent, messageId)
            yourNetiApiService.getMessages().find { it.id == messageId } ?: throw NullPointerException()
        }
    }



}
