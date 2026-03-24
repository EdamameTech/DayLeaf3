package com.edamametech.android.dayleaf3.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun noteDateString(noteDate: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd E", Locale.US)
    return noteDate.format(formatter)
}