package com.bible.core.roomDatabase.roomDataTable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "CommentariesTableDatabase")
data class CommentariesTableValue(
    @PrimaryKey
    @ColumnInfo(name = "commentaryid") var commentaryId: String,
    @ColumnInfo(name = "commentaryTitle") var commentaryTitle: String,
    @ColumnInfo(name = "commentary") var commentary: String

)