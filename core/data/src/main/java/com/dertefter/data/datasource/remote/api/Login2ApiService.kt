package com.dertefter.data.datasource.remote.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Login2ApiService {


    @GET(".")
    suspend fun getLogin2FormParamsAuth(): ResponseBody


    @FormUrlEncoded
    @POST("realms/master/login-actions/authenticate")
    suspend fun authenticate(
        @Query("session_code") sessionCode: String,
        @Query("execution") execution: String,
        @Query("client_id") clientId: String,
        @Query("tab_id") tabId: String,
        @Query("client_data") clientData: String,
        @Field("username") username: String,
        @Query("selected_subset") selectedSubset: String = "",
        @Field("username-visible") usernameVisible: String,
        @Field("password") password: String,
        @Field("credentialId") credentialId: String = "",
    ): Response<ResponseBody>
}