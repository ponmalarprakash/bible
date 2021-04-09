package com.bible.roomDatabase

import androidx.room.TypeConverter
import com.bible.modeldata.commentarydownloadpage.Commentaries
import com.bible.modeldata.commentarydownloadpage.CommentariesResponse
import com.bible.modeldata.homepage.BibleHomePageResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Type converter class to convert custom objects into string and vice-versa
 *
 * Room database supports only primitive date types like Int, String, Boolean, Long, Float and Double; Other than these should converted using @TypeConverter annotation.
 *
 */

class Converters {
    @TypeConverter
    fun getVersesValueFromModel(data: BibleHomePageResponse): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun getCommentariesValueFromModel(data: CommentariesResponse): String {
        return Gson().toJson(data)
    }



    @TypeConverter
    fun getModelFromCommentaryString(data: String):CommentariesResponse {
        val type = object : TypeToken<CommentariesResponse>() {}.type
        return Gson().fromJson(data,type)
    }
}