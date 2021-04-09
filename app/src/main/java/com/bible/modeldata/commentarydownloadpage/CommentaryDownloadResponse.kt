package com.bible.modeldata.commentarydownloadpage

data class CommentaryDownloadResponse(
    val commentaries: List<Commentary>,
    val message: String,
    val status: Int
)

data class Commentary(
    val id: String,
    val status: String,
    val title: String
)