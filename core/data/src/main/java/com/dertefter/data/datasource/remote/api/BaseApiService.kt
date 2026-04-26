package com.dertefter.data.datasource.remote.api

import com.dertefter.data.dto.schedule.SearchGroupResponseDto
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BaseApiService {

    @GET(".")
    suspend fun getNews(@Query("main_events") page: String): ResponseBody

    @FormUrlEncoded
    @POST("calendar")
    suspend fun getEvents(
        @Field("period") period: String = "0",
        @Field("year") year: String,
        @Field("month") month: String,
        @Field("day") day: String = "1",
        @Field("act") act: String = "0",
    ): ResponseBody

    @GET("news/news_more")
    suspend fun getNewsDetail(@Query("idnews") id: String): ResponseBody

    @GET("studies/schedule/schedule_classes/schedule")
    suspend fun getSchedule(@Query("group") group: String, @Query("week") week: String = "1", @Query("print") print: String = "true"): ResponseBody


    @GET("studies/schedule/schedule_session/schedule")
    suspend fun getSessiaSchedule(@Query("group") group: String, @Query("print") print: String = "true"): ResponseBody

    @GET("studies/schedule/schedule_classes/schedule")
    suspend fun getScheduleForFirstDay(@Query("group") group: String, @Query("week") week: String = "1"): ResponseBody


    @GET("studies/schedule/schedule_classes")
    suspend fun getSearchGroupResults(@Query("query") query: String): SearchGroupResponseDto

    @GET("phone/object")
    suspend fun getSearchPersonResults(@Query("search_term") searchTerm: String): ResponseBody

    @GET(".")
    suspend fun getBasePage(): ResponseBody


}
