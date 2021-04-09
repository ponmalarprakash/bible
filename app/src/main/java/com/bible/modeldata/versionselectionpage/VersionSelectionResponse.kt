package com.bible.modeldata.versionselectionpage

data class VersionSelectionResponse(

    val message: String,
    val status: Int,
    val versions: List<Version>
)

data class Version(
    var isDownloading: Boolean = false,
    var progress: Int = 0,
    val id: String,
    val name: String,
    val slug: String
)