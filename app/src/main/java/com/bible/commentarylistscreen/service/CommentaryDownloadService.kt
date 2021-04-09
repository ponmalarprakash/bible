package com.bible.commentarylistscreen.service

import com.bible.modeldata.commentarydownloadpage.CommentariesResponse
import com.bible.modeldata.commentarydownloadpage.CommentaryDownloadResponse
import retrofit2.Call
import retrofit2.http.*

interface CommentaryDownloadService {
    @GET("verses/commentarylist")
    fun getCommentaryData(
        @Header(value = "Authorization") auth: String): Call<CommentaryDownloadResponse>


    @Streaming
    @GET("verses/commentaries/"+"{id}")
    fun getCommentariesApi(
        @Header(value = "Authorization") auth: String, @Path("id") id: String): Call<CommentariesResponse>
}