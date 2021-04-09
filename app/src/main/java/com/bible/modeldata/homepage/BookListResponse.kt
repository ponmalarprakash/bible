package com.bible.modeldata.homepage

data class BookListResponse(
    val books: Books,
    val message: String,
    val status: Int
)

data class Books(
    val after: List<After>,
    val before: List<Before>
)

data class After(
    val christ: Any,
    val id: String,
    val name: String,
    val short_name: String,
    val slug: String,
    var isSelected: Boolean = false
)

data class Before(
    val christ: Any,
    val id: String,
    val name: String,
    val short_name: String,
    val slug: String,
    var isSelected: Boolean = false
)