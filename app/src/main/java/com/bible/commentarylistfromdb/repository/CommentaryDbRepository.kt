package com.bible.commentarylistfromdb.repository

import android.annotation.SuppressLint
import android.content.Context
import com.bible.commentarylistfromdb.service.CommentaryDbService
import com.bible.commentaryscreen.service.CommentaryService
import com.bible.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class CommentaryDbRepository private constructor(var mContext: Context){
    companion object {
        // For Singleton instantiation
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: CommentaryDbRepository? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: CommentaryDbRepository(context).also {
                instance = it
            }
        }

        internal val commentaryDbJob = Job()
        private val backgroundScope = CoroutineScope(Dispatchers.IO + commentaryDbJob)
        private val foregroundScope = CoroutineScope(Dispatchers.Main)



    }
    private val commentaryDbService: CommentaryDbService by lazy {
        AppController.getInstance()
            .serviceClass(mContext, CommentaryDbService::class.java, CommonData.Base_url)
    }












}