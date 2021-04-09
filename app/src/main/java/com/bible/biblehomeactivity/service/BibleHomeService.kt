package com.bible.biblehomeactivity.service

import com.bible.modeldata.homepage.BibleHomePageResponse
import com.bible.modeldata.homepage.BookListResponse
import com.bible.modeldata.homepage.ChapterListResponse
import com.bible.modeldata.homepage.TitlesListResponse
import retrofit2.Call
import retrofit2.http.*

interface BibleHomeService {
    @Streaming
    @FormUrlEncoded
    @POST("verses/download")
    fun getBibleData(
        @Header(value = "Authorization") auth: String,
        @Field("version") versionID: String): Call<BibleHomePageResponse>

    @GET("verses/books")
    fun getBooksListApi(
        @Header(value = "Authorization") auth: String): Call<BookListResponse>

    @FormUrlEncoded
    @POST("verses/chapters")
    fun getBibleChapterData(
        @Header(value = "Authorization") auth: String,
        @Field("book_id") bookID: String
    ): Call<ChapterListResponse>

    @FormUrlEncoded
    @POST("verses/titles")
    fun getTitleData(
        @Header(value = "Authorization") auth: String,
        @Field("book_id") bookID: String,
        @Field("chapter_id") chapterID: String
    ): Call<TitlesListResponse>


}