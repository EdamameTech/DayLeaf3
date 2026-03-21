package com.edamametech.android.dayleaf3.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
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

    val text: String,

    val isExported: Boolean
)
