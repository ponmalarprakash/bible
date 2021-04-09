package com.bible.splashactivity.service

import com.bible.modeldata.splashpage.SplashResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SplashService {
    @FormUrlEncoded
    @POST("auth/login")
    fun getTokenApi(
        @Field("deviceID") deviceID: String
    ): Call<SplashResponse>
}