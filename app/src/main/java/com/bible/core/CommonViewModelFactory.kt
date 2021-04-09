package com.bible.core

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bible.biblehomeactivity.repository.BibleHomeRepository
import com.bible.biblehomeactivity.view.BibleHomeActivity
import com.bible.biblehomeactivity.viewmodel.BibleHomeViewModel
import com.bible.commentarylistfromdb.repository.CommentaryDbRepository
import com.bible.commentarylistfromdb.view.CommentaryDbActivity
import com.bible.commentarylistfromdb.viewmodel.CommentaryDbViewModel
import com.bible.commentarylistscreen.repository.CommentaryDownloadRepository
import com.bible.commentarylistscreen.view.CommentaryDownloadActivity
import com.bible.commentarylistscreen.viewmodel.CommentaryDownloadViewModel
import com.bible.commentaryscreen.repository.CommentaryRepository
import com.bible.commentaryscreen.view.CommentaryActivity
import com.bible.commentaryscreen.viewmodel.CommentaryViewModel
import com.bible.splashactivity.repository.SplashRepository
import com.bible.splashactivity.view.SplashActivity
import com.bible.splashactivity.viewmodel.SplashViewModel
import com.bible.referencescreen.repository.ReferenceRepository
import com.bible.referencescreen.view.ReferenceActivity
import com.bible.referencescreen.viewmodel.ReferenceScreenViewModel
import com.bible.todayscreen.repository.TodayRepository
import com.bible.todayscreen.view.TodayActivity
import com.bible.todayscreen.viewmodel.TodayViewModel
import com.bible.versionselectionactivity.repository.VersionSelectionRepository
import com.bible.versionselectionactivity.view.VersionSelectionActivity
import com.bible.versionselectionactivity.viewmodel.VersionSelectionViewModel

class CommonViewModelFactory(private val context: Context, private val viewModelType: Int = 0) :
    ViewModelProvider.Factory {

    private fun getLoginRepository(context: Context): SplashRepository =
        SplashRepository.getInstance(context)

    private fun getVersionSelectionRespository(context: Context): VersionSelectionRepository =
        VersionSelectionRepository.getInstance(context)

    private fun getBibleHomePageRespository(context: Context): BibleHomeRepository =
        BibleHomeRepository.getInstance(context)

    private fun getReferenceScreenRespository(context: Context): ReferenceRepository =
        ReferenceRepository.getInstance(context)


    private fun getCommentaryDownlaodRepository(context: Context): CommentaryDownloadRepository =
        CommentaryDownloadRepository.getInstance(context)

    private fun getCommentaryRepository(context: Context): CommentaryRepository =
        CommentaryRepository.getInstance(context)

    private fun getCommentaryDbRepository(context: Context): CommentaryDbRepository =
        CommentaryDbRepository.getInstance(context)

    private fun getTodayRepository(context: Context): TodayRepository =
        TodayRepository.getInstance(context)


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (context) {


            is SplashActivity -> SplashViewModel(
                getLoginRepository(context),
                context.applicationContext as AppController
            ) as T

            is VersionSelectionActivity -> VersionSelectionViewModel(
                getVersionSelectionRespository(context),
                context.applicationContext as AppController
            ) as T

            is BibleHomeActivity -> BibleHomeViewModel(
                getBibleHomePageRespository(context),
                context.applicationContext as AppController
            ) as T

            is ReferenceActivity -> ReferenceScreenViewModel(
                getReferenceScreenRespository(context),
                context.applicationContext as AppController
            ) as T


            is CommentaryDownloadActivity -> CommentaryDownloadViewModel(
                getCommentaryDownlaodRepository(context),
                context.applicationContext as AppController
            ) as T

            is CommentaryActivity -> CommentaryViewModel(
                getCommentaryRepository(context),
                context.applicationContext as AppController
            ) as T

            is CommentaryDbActivity -> CommentaryDbViewModel(
                getCommentaryDbRepository(context),
                context.applicationContext as AppController
            ) as T

            is TodayActivity -> TodayViewModel(
                getTodayRepository(context),
                context.applicationContext as AppController
            ) as T


            else -> SplashViewModel(
                getLoginRepository(context),
                context.applicationContext as AppController
            ) as T
        }
    }
}

