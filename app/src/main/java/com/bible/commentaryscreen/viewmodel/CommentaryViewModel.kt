package com.bible.commentaryscreen.viewmodel

import android.app.Application
import com.bible.commentaryscreen.repository.CommentaryRepository
import com.bible.core.BaseViewModel

class CommentaryViewModel( var commentaryRepository: CommentaryRepository,
var context: Application) :
BaseViewModel(context) {


}
