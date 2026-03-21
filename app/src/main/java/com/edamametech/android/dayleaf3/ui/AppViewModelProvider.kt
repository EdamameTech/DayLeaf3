package com.edamametech.android.dayleaf3.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.edamametech.android.dayleaf3.DayLeaf3Application

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            NoteViewModel(
                this.createSavedStateHandle(),
                dayLeaf3Application().container.notesRepository
            )
        }
    }
}

fun CreationExtras.dayLeaf3Application(): DayLeaf3Application =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DayLeaf3Application)