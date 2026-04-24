package com.dertefter.data.datasource.remote.api

import com.dertefter.data.dto.auth.AuthYourNetiRequest
import com.dertefter.data.dto.auth.AuthYourNetiResponse
import com.dertefter.data.dto.messsages.MessageDto
import com.dertefter.data.dto.messsages.MoveMessageToTrashRequest
import com.dertefter.data.dto.messsages.ReadMessageRequest
import com.dertefter.data.dto.messsages.RemoveMessageFromTrashRequest
import com.dertefter.data.dto.messsages.UnreadMessageRequest
import com.dertefter.data.dto.sessia_results.SessiaResultDto
import com.dertefter.data.dto.user.UserInfoDto
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface YourNetiApiService {

    @POST("token/new-auth")
    suspend fun newAuth(
        @Query("disableNotification") disableNotification: Boolean = true,
        @Body body: AuthYourNetiRequest
    ): AuthYourNetiResponse


    @GET("student/get_data/app/get_user_info")
    suspend fun getUserInfo(): List<UserInfoDto>

    @GET("student/get_data/app/get_messages")
    suspend fun getMessages(
        @Query("start_date") startDate: String = "1990-01-01T"
    ): List<MessageDto>

    @GET("student/get_data/app/get_session_marks")
    suspend fun getSessiaResults(): List<SessiaResultDto>

    @PUT("student/modify_data/app/update_message_deleted_status")
    suspend fun moveMessageToTrash(
        @Body body: MoveMessageToTrashRequest
    ): Unit

    @PUT("student/modify_data/app/update_message_deleted_status")
    suspend fun removeMessageFromTrash(
        @Body body: RemoveMessageFromTrashRequest
    ): Unit

    @PUT("student/modify_data/app/update_message_read_status")
    suspend fun readMessage(
        @Body body: ReadMessageRequest
    ): Unit

    @PUT("student/modify_data/app/update_message_read_status")
    suspend fun unreadMessage(
        @Body body: UnreadMessageRequest
    ): Unit

}