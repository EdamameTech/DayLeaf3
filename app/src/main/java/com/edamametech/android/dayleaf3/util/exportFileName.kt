package com.edamametech.android.dayleaf3.util

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun exportFileName(currentTimeMillis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmm").withZone(ZoneOffset.UTC)
    val dateString = formatter.format(Instant.ofEpochMilli(currentTimeMillis))
    return "dayleaf3-${dateString}Z.txt"
}