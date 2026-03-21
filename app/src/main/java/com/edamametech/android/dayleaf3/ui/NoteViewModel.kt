package com.edamametech.android.dayleaf3.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edamametech.android.dayleaf3.data.NotesRepository
import com.edamametech.android.dayleaf3.data.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class NoteUiState(
    val date: LocalDate? = null,
    val text: String = "",

    val allDates: List<LocalDate> = emptyList<LocalDate>(),
    val anyExportable: Boolean = false,
    val isFirstDate: Boolean = true,
    val isToday: Boolean = true,
    val isEdited: Boolean = false
)

class NoteViewModel(
    savesStateHandle: SavedStateHandle,
    private val notesRepository: NotesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadNote(LocalDate.now())
        }
    }

    suspend fun loadNote(date: LocalDate) {
        val allDates = notesRepository.getAllDates()
        val isToday = date.isEqual(LocalDate.now())
        val isFirstDate = date.isEqual(allDates[0])

        val note = notesRepository.getNote(date)
        _uiState.update { currentState ->
            currentState.copy(
                date = date,
                text = if (note != null) {
                    note.text
                } else {
                    ""
                },
                allDates = allDates,
                isFirstDate = isFirstDate,
                isToday = isToday,
            )
        }
    }

    suspend fun saveAndSetDate(date: LocalDate) {
        saveNote()
        loadNote(date)
    }

    suspend fun offsetDate(offset: Int) {
        var t = uiState.value.allDates.binarySearch(uiState.value.date) + offset
        if (t < 0) {
            t = 0
        }
        if (t >= uiState.value.allDates.size) {
            t = uiState.value.allDates.size - 1
        }
        saveAndSetDate(uiState.value.allDates[t])
    }

    fun updateNote(text: String) {
        _uiState.update { currentState ->
            currentState.copy(
                text = text,
                isEdited = true
            )
        }
    }

    suspend fun saveNote() {
        if (uiState.value.date != null && uiState.value.isEdited) {
            notesRepository.upsertNote(
                Note(
                    uiState.value.date!!,
                    uiState.value.text,
                    isExported = false
                )
            )
        }
    }
}
