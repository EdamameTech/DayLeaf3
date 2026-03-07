package com.edamametech.android.dayleaf3.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface NotesRepository {
    fun getAllDatesStream(): Flow<List<LocalDate>>

    suspend fun upsertNote(note: Note)
}