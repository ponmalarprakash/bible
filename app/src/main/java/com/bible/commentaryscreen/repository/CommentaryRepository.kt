package com.bible.commentaryscreen.repository

import android.annotation.SuppressLint
import android.content.Context
import com.bible.commentaryscreen.service.CommentaryService
import com.bible.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class CommentaryRepository private constructor(var mContext: Context){
    companion object {
        // For Singleton instantiation
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: CommentaryRepository? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: CommentaryRepository(context).also {
                instance = it
            }
        }

        internal val commentaryJob = Job()
        private val backgroundScope = CoroutineScope(Dispatchers.IO + commentaryJob)
        private val foregroundScope = CoroutineScope(Dispatchers.Main)



    }
    private val versionService: CommentaryService by lazy {
        AppController.getInstance()
            .serviceClass(mContext, CommentaryService::class.java, CommonData.Base_url)
    }












}