package com.bible.modeldata.homepage


data class ChapterListResponse(
    val chapters: List<Chapter>,
    val message: String,
    val status: Int
)

data class Chapter(
    val bookId: String,
    val bookName: String,
    val book_id: String,
    val created_date: String,
    val description: String,
    val id: String,
    val name: String,
    val slug: String,
    val status: String,
    val updated_date: String,
    val versionId: Any,
    val versionName: Any,
    val version_id: Any,
    var isSelected: Boolean = false
)
