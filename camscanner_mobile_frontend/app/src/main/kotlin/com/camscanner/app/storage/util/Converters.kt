package com.camscanner.app.storage.util

import androidx.room.TypeConverter
import java.util.Date

/**
 * PUBLIC_INTERFACE
 * Converters
 * Room type converters for Date.
 */
object Converters {
    @TypeConverter
    @JvmStatic
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }
    @TypeConverter
    @JvmStatic
    fun dateToTimestamp(date: Date?): Long? = date?.time
}
