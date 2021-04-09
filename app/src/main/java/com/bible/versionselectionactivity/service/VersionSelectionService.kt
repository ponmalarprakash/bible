package com.bible.versionselectionactivity.service

import com.bible.modeldata.homepage.BibleHomePageResponse
import com.bible.modeldata.versionselectionpage.VersionSelectionResponse
import retrofit2.Call
import retrofit2.http.*

interface VersionSelectionService {
    @GET("verses/versions")
    fun getSloganListApi(
        @Header(value = "Authorization") auth: String): Call<VersionSelectionResponse>

    @Streaming
    @FormUrlEncoded
    @POST("verses/download")
    fun getBibleData(
        @Header(value = "Authorization") auth: String,
        @Field("version") versionID: String): Call<BibleHomePageResponse>




   /* @Streaming
    @GET("open/getAllVerses/5")
    fun getBibleData(): Call<BibleHomePageResponse>*/
}