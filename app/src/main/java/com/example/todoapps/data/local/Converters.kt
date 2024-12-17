package com.example.todoapps.data.local

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun dateToTimeStamp(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
}