package com.edamametech.android.dayleaf3.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey
    val date: LocalDate,

    @ColumnInfo(defaultValue = "")
    val note: String,

    @ColumnInfo(defaultValue = "false")
    val isExported: Boolean
)
