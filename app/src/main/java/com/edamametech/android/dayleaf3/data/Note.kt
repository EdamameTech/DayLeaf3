package com.edamametech.android.dayleaf3.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun fromLocalDate(localDate: LocalDate): String? = localDate?.toString()

    @TypeConverter
    fun localDateFromString(string: String?): LocalDate? = LocalDate.parse(string)
}

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey
    val date: LocalDate,

    val note: String,

    val isExported: Boolean
)
