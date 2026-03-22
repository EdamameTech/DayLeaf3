package com.edamametech.android.dayleaf3.data

import java.time.LocalDate

class NotesRepository(private val noteDao: NoteDao) {
    fun upsertNote(note: Note) = noteDao.upsertNote(note)

    fun getAllDates(): List<LocalDate> = noteDao.getAllDates()

    fun getNote(date: LocalDate): Note? = noteDao.getNote(date)

    fun getUnexportedDates(): List<LocalDate> = noteDao.getUnexportedDates()

    fun getUnexportedDatesCount(): Int = noteDao.getUnexportedDatesCount()
}