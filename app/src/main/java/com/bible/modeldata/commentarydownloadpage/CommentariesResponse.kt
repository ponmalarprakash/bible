package com.bible.modeldata.commentarydownloadpage



data class CommentariesResponse(
    val commentaries: Commentaries,
    val message: String,
    val status: Int
)

data class Commentaries(
    val books: List<Book>,
    val chapters: List<Chapter>,
    val verses: List<Verse>
)

data class Book(
    val bookId: String,
    val bookName: String,
    val book_id: String,
    val commentary: String,
    val commentaryId: String,
    val commentaryTitle: String,
    val commentary_id: String,
    val created_date: String,
    val id: String,
    val status: String,
    val updated_date: String
)

data class Chapter(
    val bookId: String,
    val bookName: String,
    val book_id: String,
    val chapterId: String,
    val chapterName: String,
    val chapter_id: String,
    val commentary: String,
    val commentaryId: String,
    val commentaryTitle: String,
    val commentary_id: String,
    val created_date: String,
    val id: String,
    val status: String,
    val updated_date: String
)

data class Verse(
    val bookId: String,
    val bookName: String,
    val book_id: String,
    val chapterId: String,
    val chapterName: String,
    val chapter_id: String,
    val commentary: String,
    val commentaryId: String,
    val commentaryTitle: String,
    val commentary_id: String,
    val created_date: String,
    val id: String,
    val status: String,
    val updated_date: String,
    val versesName: String,
    val version_id: String
)