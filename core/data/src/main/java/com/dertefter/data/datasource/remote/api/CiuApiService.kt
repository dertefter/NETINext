package com.dertefter.data.datasource.remote.api

import com.dertefter.data.dto.share_score.ShareScoreResponse
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CiuApiService {
    @GET("student_study/personal/contact_info")
    suspend fun getContactInfo(): ResponseBody

    @GET("student_study/timetable/timetable_lessons")
    suspend fun getSchedule(
        @Query("print") print: String = "true",
    ): ResponseBody

    @FormUrlEncoded
    @POST("student_study/personal/contact_info")
    suspend fun saveContactInfo(
        @Field("save") save: String = "1",
        @Field("what") what: String = "0",
        @Field("save_oms") saveOms: String = "",
        @Field("n_email") email: String,
        @Field("n_address") address: String,
        @Field("n_phone") phone: String,
        @Field("n_snils") snils: String,
        @Field("n_oms") oms: String,
        @Field("n_vk") vk: String,
        @Field("n_tg") tg: String,
        @Field("n_leader") leader: String,
        @Field("n_has_agree") hasAgree: String = ""
    ): ResponseBody

    @FormUrlEncoded
    @POST("student_study/mess_teacher")
    suspend fun getUndeletedMessages(
        @Query("year") year: Int = -1,
        @Field("what") what: String = "0",
        @Field("delid") delid: String = "0",
        @Field("vid_sort") vid_sort: String = "1",
        @Field("active_vkl") active_vkl: String = "1",
    ): ResponseBody

    @FormUrlEncoded
    @POST("student_study/mess_teacher")
    suspend fun getDeletedMessages(
        @Query("year") year: Int = -1,
        @Field("what") what: String = "1",
        @Field("delid") delid: String = "0",
        @Field("vid_sort") vid_sort: String = "1",
        @Field("active_vkl") active_vkl: String = "1",
    ): ResponseBody

    @GET("student_study/mess_teacher/view")
    suspend fun getMessageById(
        @Query("id") id: Long
    ): ResponseBody


    @FormUrlEncoded
    @POST("student_study/mess_teacher/view")
    suspend fun moveMessageToTrash(
        @Query("id") id: Long,

        @Field("what_do") whatDoField: String = "1",
        @Field("what") whatField: String = "",
        @Field("id") idField: String = id.toString()
    ): ResponseBody


    @FormUrlEncoded
    @POST("student_study/mess_teacher/view")
    suspend fun removeMessageFromTrash(
        @Query("id") id: Long,
        @Field("what_do") whatDoField: String = "2",
        @Field("what") whatField: String = "",
        @Field("id") idField: String = id.toString()
    ): ResponseBody

    @FormUrlEncoded
    @POST("student_study/mess_teacher/view")
    suspend fun deleteMessageForever(
        @Query("id") id: Long,
        @Field("what_do") whatDoField: String = "3",
        @Field("what") whatField: String = "",
        @Field("id") idField: String = id.toString()
    ): ResponseBody



    @GET("/kaf/persons/{id}")
    suspend fun getPersonById(@Path("id") id: Long): ResponseBody

    @GET("student_study/")
    suspend fun fetchBasePage(): ResponseBody

    @GET("student_study/student_info/progress")
    suspend fun getSessiaResults(): ResponseBody

    @FormUrlEncoded
    @POST("student_study/student_info/link_progress")
    suspend fun generateShareScoreLink(
        @Field("generate_access_url") generateAccessUrl: Boolean = false,
    ): ShareScoreResponse

    @GET("student_study/student_info/link_progress")
    suspend fun getShareScoreLink(): ResponseBody

    @FormUrlEncoded
    @POST("student_study/personal/money")
    suspend fun getMoneyForYear(
        @Field("year") year: String,
        @Field("month") month: Int = 0
    ): ResponseBody

    @GET("student_study/personal/money")
    suspend fun getMoneyYears(): ResponseBody



}