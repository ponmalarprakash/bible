package com.bible.roomDatabase

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bible.core.roomDatabase.roomDataTable.CommentariesTableValue
import com.bible.core.roomDatabase.roomDataTable.VersesTableValue

@Dao
interface SampleDataDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVersesValue(vararg user: VersesTableValue)

    @Query("SELECT * FROM VersesTableDatabase")
    fun getVersesValue(): LiveData<String>


    //commentaries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCommentariesValue(vararg user: CommentariesTableValue)

    @Query("SELECT commentaryTitle FROM CommentariesTableDatabase")
    fun getCommentaryValue(): LiveData<List<String>>


    @Query("SELECT commentary FROM CommentariesTableDatabase WHERE commentaryTitle = :commentaryTitle")
    fun getCommentaryData(commentaryTitle: String): LiveData<String>


}