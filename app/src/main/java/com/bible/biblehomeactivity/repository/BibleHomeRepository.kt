package com.bible.biblehomeactivity.repository

import android.annotation.SuppressLint
import android.content.Context
import com.bible.biblehomeactivity.service.BibleHomeService
import com.bible.core.*
import com.bible.modeldata.homepage.BibleHomePageResponse
import com.bible.modeldata.homepage.BookListResponse
import com.bible.modeldata.homepage.ChapterListResponse
import com.bible.modeldata.homepage.TitlesListResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BibleHomeRepository private constructor(var mContext: Context)
{
    companion object {
        // For Singleton instantiation
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: BibleHomeRepository? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: BibleHomeRepository(context).also {
                instance = it
            }
        }

        internal val bibleContentDownloadJob = Job()
        private val backgroundScope = CoroutineScope(Dispatchers.IO + bibleContentDownloadJob)
        private val foregroundScope = CoroutineScope(Dispatchers.Main)



    }
    private val bibleHomeService: BibleHomeService by lazy {
        AppController.getInstance()
            .serviceClass(mContext, BibleHomeService::class.java, CommonData.Base_url)
    }

    fun getBibleDataApicall(
        authToken: String,versionId:String,
        callback: TaskCallback<BibleHomePageResponse>
    ) {
        backgroundScope.launch {
            when (val result: ApiResult<BibleHomePageResponse> =
                bibleHomeService.getBibleData(
                    "Bearer "+authToken,versionId).awaitResult()) {
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

    fun getBooksListApi(
        authToken: String,
        callback: TaskCallback<BookListResponse>
    ) {
       backgroundScope.launch {
            when (val result: ApiResult<BookListResponse> =
                bibleHomeService.getBooksListApi(
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

    fun getBibleChapterApicall(
        authToken: String, bookId: String,
        callback: TaskCallback<ChapterListResponse>
    ) {
        backgroundScope.launch {
            when (val result: ApiResult<ChapterListResponse> =
                bibleHomeService.getBibleChapterData(
                    "Bearer " + authToken, bookId
                ).awaitResult()) {
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

    fun getBibleTitlesApicall(
        authToken: String, bookId: String,chapterId: String,
        callback: TaskCallback<TitlesListResponse>
    ) {
        backgroundScope.launch {
            when (val result: ApiResult<TitlesListResponse> =
                bibleHomeService.getTitleData(
                    "Bearer " + authToken, bookId,chapterId
                ).awaitResult()) {
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