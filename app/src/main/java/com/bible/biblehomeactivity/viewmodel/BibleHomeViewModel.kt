package com.bible.biblehomeactivity.viewmodel

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bible.biblehomeactivity.repository.BibleHomeRepository
import com.bible.core.BaseViewModel
import com.bible.core.ConnectivityReceiver
import com.bible.core.TaskCallback
import com.bible.modeldata.homepage.BibleHomePageResponse
import com.bible.modeldata.homepage.BookListResponse
import com.bible.modeldata.homepage.ChapterListResponse
import com.bible.modeldata.homepage.TitlesListResponse
import kotlin.math.roundToInt

class BibleHomeViewModel(var bibleHomeRepository: BibleHomeRepository, var context: Application) :
    BaseViewModel(context) {
    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    private val _refreshData = MutableLiveData<Boolean>()
    val refreshData: LiveData<Boolean>
        get() = _refreshData

    private val _fetchData = MutableLiveData<Boolean>()
    val fetchData: LiveData<Boolean>
        get() = _fetchData


    private val _bibleHomePageResponse = MutableLiveData<BibleHomePageResponse>()
    val bibleHomePageResponse: LiveData<BibleHomePageResponse>
        get() = _bibleHomePageResponse

    private val _bookListResponse = MutableLiveData<BookListResponse>()
    val bookListResponse: LiveData<BookListResponse>
        get() = _bookListResponse

    private val _chapterListResponse = MutableLiveData<ChapterListResponse>()
    val chapterListResponse: LiveData<ChapterListResponse>
        get() = _chapterListResponse


    private val _titlesListResponse = MutableLiveData<TitlesListResponse>()
    val titlesListResponse: LiveData<TitlesListResponse>
        get() = _titlesListResponse

    fun getBibleData(authtToken: String, versionId: String) {
        if (ConnectivityReceiver.isOnline) {
            _refreshData.value = true
            bibleHomeRepository.getBibleDataApicall(
                authtToken, versionId, object :
                    TaskCallback<BibleHomePageResponse> {

                    override fun onComplete(result: BibleHomePageResponse) {


                        val data = ByteArray(result.toString().length)
                        var offset = 0

                        val currentRead =
                            result.toString().byteInputStream().read(data, offset, data.size)
                        offset += currentRead
                        val progress = (offset * 100f / data.size).roundToInt()

                        println("siiiiiva::::" + "success" + progress)

                        if (progress == 100) {

                            Handler(Looper.getMainLooper()).postDelayed({
                                _bibleHomePageResponse.value = result
                                _refreshData.value = false

                            },2000)

                        }
                    }

                    override fun onException(t: Throwable?) {
                        _refreshData.value = false
                    }
                })
        }
    }


    fun getBooks(authToken: String) {
        if (ConnectivityReceiver.isOnline) {
            _fetchData.value = true
            bibleHomeRepository.getBooksListApi(
                authToken, object :
                    TaskCallback<BookListResponse> {

                    override fun onComplete(result: BookListResponse) {
                        _bookListResponse.value = result
                        _fetchData.value = false
                    }

                    override fun onException(t: Throwable?) {
                        _fetchData.value = false
                    }
                })
        }
    }

    fun getChapterList(authtToken: String, bookId: String) {
        if (ConnectivityReceiver.isOnline) {
            _fetchData.value = true
            bibleHomeRepository.getBibleChapterApicall(
                authtToken, bookId, object :
                    TaskCallback<ChapterListResponse> {

                    override fun onComplete(result: ChapterListResponse) {


                        _chapterListResponse.value = result
                        _fetchData.value = false


                    }

                    override fun onException(t: Throwable?) {
                        _fetchData.value = false
                    }
                })
        }
    }

    fun getTitlesList(authtToken: String, bookId: String,chapterId: String) {
        if (ConnectivityReceiver.isOnline) {
            _fetchData.value = true
            bibleHomeRepository.getBibleTitlesApicall(
                authtToken, bookId, chapterId,object :
                    TaskCallback<TitlesListResponse> {

                    override fun onComplete(result: TitlesListResponse) {


                        _titlesListResponse.value = result
                        _fetchData.value = false


                    }

                    override fun onException(t: Throwable?) {
                        _fetchData.value = false
                    }
                })
        }
    }
}