package com.bible.core.roomDatabase.roomDataTable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "VersesTableDatabase")
data class VersesTableValue(
    @PrimaryKey
    @ColumnInfo(name = "VERSES") val itemId: String
)