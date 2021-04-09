package com.bible.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bible.core.roomDatabase.roomDataTable.CommentariesTableValue
import com.bible.core.roomDatabase.roomDataTable.VersesTableValue

/**
 * The Room database for this app
 */

private const val DATABASE_NAME = "BibleDatabase"

@Database(entities = [VersesTableValue::class, CommentariesTableValue::class], version = 11)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sampleDataDao(): SampleDataDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            println("getInstance")
            return instance ?: synchronized(this) {
                println("getInstance")
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            val db = Room.databaseBuilder(
                context,
                AppDatabase::class.java, DATABASE_NAME
            ).build()
            return db
        }
    }
}