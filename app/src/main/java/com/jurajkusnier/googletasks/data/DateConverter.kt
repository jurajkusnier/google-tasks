package com.jurajkusnier.googletasks.data

import androidx.room.TypeConverter
import java.util.*


class DateConverter {

    @TypeConverter
    fun toDate(dateLong: Long): Date? {
        if (dateLong == 0L) return null
        return Date(dateLong)
    }

    @TypeConverter
    fun fromDate(date: Date?): Long {
        return date?.time ?: 0L
    }
}
