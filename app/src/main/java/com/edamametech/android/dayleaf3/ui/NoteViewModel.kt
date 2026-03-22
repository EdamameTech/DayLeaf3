package com.edamametech.android.dayleaf3.ui

import androidx.compose.ui.platform.LocalAutofill
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.edamametech.android.dayleaf3.DayLeaf3Application
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

    val previousDate: LocalDate? = null,
    val nextDate: LocalDate? = null,
    val anyExportable: Boolean = false,
    val isEdited: Boolean = false
)

class NoteViewModel(
    private val notesRepository: NotesRepository,
    private val savesStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadNote(LocalDate.now())
        }
    }

    suspend fun loadNote(date: LocalDate) {
        val today = LocalDate.now()
        val allDates = notesRepository.getAllDates()
        val dateIndex = allDates.binarySearch(date)
        val previousDate = if (dateIndex > 0) {
            allDates[dateIndex - 1]
        } else {
            null
        }
        val nextDate = if (dateIndex < allDates.size - 1) {
            allDates[dateIndex + 1]
        } else {
            if (!date.isEqual(today)) {
                today
            } else {
                null
            }
        }
        val anyExportable = !notesRepository.getUnexportedDates().isEmpty()
        val note = notesRepository.getNote(date)

        _uiState.update { currentState ->
            currentState.copy(
                date = date,
                text = if (note != null) {
                    note.text
                } else {
                    ""
                },
                previousDate = previousDate,
                nextDate = nextDate,
                anyExportable = anyExportable,
                isEdited = false
            )
        }
    }

    suspend fun saveAndSetDate(date: LocalDate) {
        saveNote()
        loadNote(date)
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
        _uiState.update { currentState ->
            currentState.copy(
                isEdited = false,
                anyExportable = true
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                val savesStateHandle = extras.createSavedStateHandle()

                return NoteViewModel(
                    (application as DayLeaf3Application).container.notesRepository,
                    savesStateHandle
                ) as T
            }
        }
    }
}
