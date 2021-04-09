package com.bible.commentarylistscreen.viewmodel

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bible.commentarylistscreen.repository.CommentaryDownloadRepository
import com.bible.core.BaseViewModel
import com.bible.core.ConnectivityReceiver
import com.bible.core.TaskCallback
import com.bible.modeldata.commentarydownloadpage.CommentariesResponse
import com.bible.modeldata.commentarydownloadpage.CommentaryDownloadResponse
import kotlin.math.roundToInt

class CommentaryDownloadViewModel(
    var commentaryDownloadRepository: CommentaryDownloadRepository,
    var context: Application
) :
    BaseViewModel(context) {
    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading


    private val _refreshData = MutableLiveData<Boolean>()
    val refreshData: LiveData<Boolean>
        get() = _refreshData

    private val _commentaryDownloadResponse = MutableLiveData<CommentaryDownloadResponse>()
    val commentaryDownloadResponse: LiveData<CommentaryDownloadResponse>
        get() = _commentaryDownloadResponse

    private val _commentariesResponse = MutableLiveData<CommentariesResponse>()
    val commentariesResponse: LiveData<CommentariesResponse>
        get() = _commentariesResponse

    fun getCommentaryData(authtToken: String) {
        if (ConnectivityReceiver.isOnline) {
            _dataLoading.value = true
            commentaryDownloadRepository.getCommentaryDatacall(
                authtToken, object :
                    TaskCallback<CommentaryDownloadResponse> {

                    override fun onComplete(result: CommentaryDownloadResponse) {

                        _commentaryDownloadResponse.value = result
                        _dataLoading.value = false


                    }

                    override fun onException(t: Throwable?) {
                        _dataLoading.value = false
                    }
                })
        }
    }

    fun getCommentaries(authToken: String, commentariesID: String) {
        if (ConnectivityReceiver.isOnline) {
            _refreshData.value = true
            commentaryDownloadRepository.getCommentariesAPi(
                authToken, commentariesID, object :
                    TaskCallback<CommentariesResponse> {

                    override fun onComplete(result: CommentariesResponse) {


                        val data = ByteArray(result.toString().length)
                        var offset = 0

                        val currentRead =
                            result.toString().byteInputStream().read(data, offset, data.size)
                        offset += currentRead
                        val progress = (offset * 100f / data.size).roundToInt()

                        println("siiiiiva::::" + "success" + progress)

                        if (progress == 100) {

                            Handler(Looper.getMainLooper()).postDelayed({
                                _commentariesResponse.value = result
                                _refreshData.value = false

                            }, 2000)

                        }

                    }

                    override fun onException(t: Throwable?) {
                        _refreshData.value = false
                    }
                })
        }
    }

}