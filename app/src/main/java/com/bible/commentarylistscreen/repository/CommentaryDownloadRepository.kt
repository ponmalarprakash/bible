package com.bible.commentarylistscreen.repository

import android.annotation.SuppressLint
import android.content.Context
import com.bible.commentarylistscreen.service.CommentaryDownloadService
import com.bible.core.*
import com.bible.modeldata.commentarydownloadpage.CommentariesResponse
import com.bible.modeldata.commentarydownloadpage.CommentaryDownloadResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CommentaryDownloadRepository private constructor(var mContext: Context)
{
    companion object {
        // For Singleton instantiation
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: CommentaryDownloadRepository? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: CommentaryDownloadRepository(context).also {
                instance = it
            }
        }

        internal val commentaryDownloadJob = Job()
        private val backgroundScope = CoroutineScope(Dispatchers.IO + commentaryDownloadJob)
        private val foregroundScope = CoroutineScope(Dispatchers.Main)



    }
    private val commentaryDownloadService: CommentaryDownloadService by lazy {
        AppController.getInstance()
            .serviceClass(mContext, CommentaryDownloadService::class.java, CommonData.Base_url)
    }

    fun getCommentaryDatacall(
        authToken: String,
        callback: TaskCallback<CommentaryDownloadResponse>
    ) {
        backgroundScope.launch {
            when (val result: ApiResult<CommentaryDownloadResponse> =
                commentaryDownloadService.getCommentaryData(
                    "Bearer "+authToken).awaitResult()) {
                is ApiResult.Ok -> foregroundScope.launch {
                    callback.onComplete(result.value)
                }
                is ApiResult.Error -> foregroundScope.launch {
                    callback.onException(result.exception)
                }
                is ApiResult.Exception -> foregroundScope.launch {
                    callback.onException(result.exception)
                }
            }
        }
    }

    fun getCommentariesAPi(
        authToken: String,commentariesId:String,
        callback: TaskCallback<CommentariesResponse>
    ) {
        backgroundScope.launch {
            when (val result: ApiResult<CommentariesResponse> =
                commentaryDownloadService.getCommentariesApi(
                    "Bearer "+authToken,commentariesId).awaitResult()) {
                is ApiResult.Ok -> foregroundScope.launch {
                    callback.onComplete(result.value)
                }
                is ApiResult.Error -> foregroundScope.launch {
                    callback.onException(result.exception)
                }
                is ApiResult.Exception -> foregroundScope.launch {
                    callback.onException(result.exception)
                }
            }
        }
    }

}