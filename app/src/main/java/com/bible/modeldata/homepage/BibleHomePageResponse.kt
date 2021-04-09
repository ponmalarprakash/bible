package com.bible.modeldata.homepage



data class BibleHomePageResponse(
    val data: Data,
    val message: String,
    val status: Int
)

data class Data(
    val verse_reference: List<VerseReference>,
    val verses: List<Verse>,
    val word_reference: List<WordReference>
)


data class Verse(
    val book_id: String,
    val chapter_id: String,
    val id: String,
    val intro: Intro,
    val name: String,
    val slug: String,
    val title_id: String,
    val verse: String
)


data class WordReference(
    val id: String,
    val name: String,
    val slug: String
)



data class VerseReference(
    val id: String,
    val name: String,
    val slug: String,
    val verse_id: String
)


data class Intro(
    val content: String,
    val title: String
)