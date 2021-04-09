package com.bible.referencescreen.service

import com.bible.modeldata.referencepage.ReferenceScreenResponse
import retrofit2.Call
import retrofit2.http.*

interface ReferenceScreenService {
    @GET("verses/versiontitles/"+"{id}")
    fun getTitleListApi(
        @Header(value = "Authorization") auth: String, @Path("id") id: String): Call<ReferenceScreenResponse>





}