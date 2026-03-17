package com.edamametech.android.dayleaf3.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Note::class], version = 1)
@TypeConverters(Converters::class)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var Instance : NotesDatabase? = null

        fun getDatabase(context: Context): NotesDatabase {
             return Instance ?: synchronized(this) {
                Room.databaseBuilder(context,
                    NotesDatabase::class.java, "dayleaf3-notes.db")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}