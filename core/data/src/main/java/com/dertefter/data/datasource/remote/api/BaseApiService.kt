package com.dertefter.data.datasource.remote.api

import com.dertefter.data.dto.schedule.SearchGroupResponseDto
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface BaseApiService {

    @GET(".")
    suspend fun getNews(@Query("main_events") page: String): ResponseBody

    @GET("news/news_more")
    suspend fun getNewsDetail(@Query("idnews") id: String): ResponseBody

    @GET("studies/schedule/schedule_classes/schedule")
    suspend fun getSchedule(@Query("group") group: String, @Query("week") week: String = "1", @Query("print") print: String = "true"): ResponseBody


    @GET("studies/schedule/schedule_classes/schedule")
    suspend fun getScheduleForFirstDay(@Query("group") group: String, @Query("week") week: String = "1"): ResponseBody


    @GET("studies/schedule/schedule_classes")
    suspend fun getSearchGroupResults(@Query("query") query: String): SearchGroupResponseDto

    @GET("phone/object")
    suspend fun getSearchPersonResults(@Query("search_term") searchTerm: String): ResponseBody

    @GET(".")
    suspend fun getBasePage(): ResponseBody


}
