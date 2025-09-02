package com.camscanner.app.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.camscanner.app.storage.dao.DocumentDao
import com.camscanner.app.storage.entities.Document
import com.camscanner.app.storage.util.Converters

/**
 * PUBLIC_INTERFACE
 * AppDatabase
 * Room database providing document storage.
 */
@Database(entities = [Document::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun documentDao(): DocumentDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        // PUBLIC_INTERFACE
        fun getInstance(context: Context): AppDatabase {
            /**
             * Returns singleton Room database instance.
             */
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "camscanner.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
