package com.edamametech.android.dayleaf3.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import java.time.LocalDate

@Dao
interface NoteDao {
    @Upsert
    fun upsertNote(note: Note)

    @Query("SELECT * FROM notes WHERE date = :date")
    fun getNote(date: LocalDate): Note?

    @Query("SELECT date FROM notes ORDER BY date ASC")
    fun getAllDates(): List<LocalDate>

    @Query("SELECT date FROM notes WHERE isExported = 0 ORDER BY date ASC")
    fun getUnexportedDates(): List<LocalDate>
}