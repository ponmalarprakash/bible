package com.bible.commentarylistfromdb.viewmodel

import android.app.Application
import com.bible.commentarylistfromdb.repository.CommentaryDbRepository
import com.bible.commentaryscreen.repository.CommentaryRepository
import com.bible.core.BaseViewModel

class CommentaryDbViewModel(var commentaryDbRepository: CommentaryDbRepository,
                            var context: Application) :
BaseViewModel(context) {


}
