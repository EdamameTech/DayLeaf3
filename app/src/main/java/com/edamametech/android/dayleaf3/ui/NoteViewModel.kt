package com.edamametech.android.dayleaf3.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.edamametech.android.dayleaf3.data.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

/* Mock notes to be replaced by database */
val allNotes = hashMapOf<LocalDate, String>(
    LocalDate.of(2026, 3, 14) to "It is pie day!",
    LocalDate.of(2026, 3, 17) to "It is Saint Pattick's Day!",
    LocalDate.now() to "Hello, World! It is today!"
)

data class NoteUiState(
    val date: LocalDate? = null,
    val note: String = "",

    val allDates: List<LocalDate> = allNotes.keys.sorted(),
    val anyExportable: Boolean = false,
    val isFirstDate: Boolean = true,
    val isToday: Boolean = true
)

class NoteViewModel(
    savesStateHandle: SavedStateHandle,
    private val notesRepository: NotesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    init {
        setDate(LocalDate.now())
    }

    fun setDate(date: LocalDate) {
        var note = allNotes[date]
        if (note == null) {
            note = ""
        }

        var isToday = date.isEqual(LocalDate.now())
        var isFirstDate = date.isEqual(uiState.value.allDates[0])

        _uiState.update { currentState ->
            currentState.copy(
                date = date,
                note = note,
                isFirstDate = isFirstDate,
                isToday = isToday,
            )
        }
    }

    fun offsetDate(offset: Int) {
        var t = uiState.value.allDates.binarySearch(uiState.value.date) + offset
        if (t < 0) {
            t = 0
        }
        if (t >= uiState.value.allDates.size) {
            t = uiState.value.allDates.size - 1
        }
        setDate(uiState.value.allDates[t])
    }

    fun updateNote(text: String) {
        _uiState.update { currentState ->
            currentState.copy(
                note = text
            )
        }
    }
}
