package com.edamametech.android.dayleaf3.ui

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.edamametech.android.dayleaf3.DayLeaf3Application
import com.edamametech.android.dayleaf3.data.Note
import com.edamametech.android.dayleaf3.data.NotesRepository
import com.edamametech.android.dayleaf3.util.noteDateString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.OutputStream
import java.time.LocalDate

/* For testing
val mockNotes = listOf<Note>(
    Note(
        LocalDate.of(2026, 3, 14), "It is pie day!\n\n\n", false
    ), Note(
        LocalDate.of(2026, 3, 17), "It is Saint Patrick's Day!\n", false
    )
)
 */

data class NoteUiState(
    val date: LocalDate? = null,
    val text: String = "",

    val previousDate: LocalDate? = null,
    val nextDate: LocalDate? = null,
    val isEdited: Boolean = false,

    val unexported: Int = 0,
    val exporting: Int = 0,
    val exportError: String? = null
)

class NoteViewModel(
    private val notesRepository: NotesRepository, private val savesStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            // seedDatabase(mockNotes)
            loadNote(LocalDate.now())
        }
    }

    /* for testing
    suspend fun seedDatabase(notes: List<Note>) {
        for (note in notes) {
            notesRepository.upsertNote(note)
        }
    }
     */

    suspend fun loadNote(date: LocalDate) {
        val today = LocalDate.now()
        val allDates = notesRepository.getAllDates()
        val dateIndex = allDates.binarySearch(date)
        val previousDate = if (dateIndex < 0 && allDates.size > 0) {
            allDates[allDates.size - 1]
        } else if (dateIndex > 0) {
            allDates[dateIndex - 1]
        } else {
            null
        }
        val nextDate = if (dateIndex >= 0 && dateIndex < allDates.size - 1) {
            allDates[dateIndex + 1]
        } else {
            if (!date.isEqual(today)) {
                today
            } else {
                null
            }
        }
        val exportable = notesRepository.getUnexportedDatesCount()
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
                unexported = exportable,
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
                text = text, isEdited = true
            )
        }
    }

    fun saveNote() {
        var unexported = uiState.value.unexported
        if (uiState.value.date != null && uiState.value.isEdited) {
            notesRepository.upsertNote(
                Note(
                    uiState.value.date!!, uiState.value.text, isExported = false
                )
            )
            unexported = notesRepository.getUnexportedDatesCount()
        }
        _uiState.update { currentState ->
            currentState.copy(
                isEdited = false, unexported = unexported
            )
        }
    }

    fun exportNotes(uri: Uri?, contentResolver: ContentResolver?) {
        if (uri != null && contentResolver != null) {
            saveNote()
            var outputStream: OutputStream? = null
            try {
                outputStream = contentResolver.openOutputStream(uri)
                if (outputStream == null) {
                    throw (Exception("Could not obtain output stream"))
                }
                val dates = notesRepository.getUnexportedDates()
                val n = dates.size
                for (i in 0..n - 1) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            exporting = i
                        )
                    }
                    var note = notesRepository.getNote(dates[i])
                    if (note != null) {
                        outputStream.write("= ${noteDateString(note.date)}\n${note.text.trimEnd{it == '\n'}}\n\n".toByteArray())
                        outputStream.flush()
                        note.isExported = true
                        notesRepository.upsertNote(note)
                    }
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        exportError = e.toString()
                    )
                }
            } finally {
                outputStream?.close()
                _uiState.update { currentState ->
                    currentState.copy(
                        exporting = 0,
                    )
                }
            }
        }
    }

    fun dismissError() {
        _uiState.update { currentState ->
            currentState.copy(
                exportError = null
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                val savesStateHandle = extras.createSavedStateHandle()

                return NoteViewModel(
                    (application as DayLeaf3Application).container.notesRepository, savesStateHandle
                ) as T
            }
        }
    }
}
