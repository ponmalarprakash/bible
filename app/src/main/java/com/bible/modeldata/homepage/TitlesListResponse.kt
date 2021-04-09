package com.bible.modeldata.homepage


data class TitlesListResponse(
    val message: String,
    val status: Int,
    val titles: List<Title>
)

data class Title(
    val bookId: String,
    val bookName: String,
    val book_id: String,
    val chapterId: String,
    val chapterName: String,
    val chapter_id: String,
    val created_date: String,
    val description: String,
    val id: String,
    val status: String,
    val title: String,
    val updated_date: String
)