package com.edamametech.android.dayleaf3.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.edamametech.android.dayleaf3.data.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

data class NoteUiState(
    val date: LocalDate = LocalDate.now(),
    val note: String = "",
)

class NoteViewModel(
    savesStateHandle: SavedStateHandle,
    private val notesRepository: NotesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    fun setDate(date: LocalDate) {
        _uiState.update { currentState ->
            currentState.copy(
                date = date,
                note = ""
            )
        }
    }
}
