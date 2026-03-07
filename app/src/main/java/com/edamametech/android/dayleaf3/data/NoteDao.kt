package com.edamametech.android.dayleaf3.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface NoteDao {
    @Upsert
    fun upsertNote(note: Note)

    @Query("SELECT * FROM notes WHERE date = :date")
    fun getNote(date: LocalDate): Flow<Note>

    @Query("SELECT date FROM notes ORDER BY date ASC")
    fun getAllDates(): Flow<List<LocalDate>>
}