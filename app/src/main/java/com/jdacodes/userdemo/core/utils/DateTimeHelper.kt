package com.jdacodes.userdemo.core.utils

import android.annotation.SuppressLint
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@SuppressLint("NewApi")
fun String.toFormattedDate(pattern: String = "dd MMM yyyy, HH:mm:ss"): String {
    // Check if the string is not null and not empty before parsing
    return if (!this.isNullOrEmpty()) {
        try {
            // Parse the string using ZonedDateTime (since it ends with 'Z' for UTC)
            val parsedDate = ZonedDateTime.parse(this)

            // Format the parsed date using a custom pattern
            val formatter = DateTimeFormatter.ofPattern(pattern)
            parsedDate.format(formatter)
        } catch (e: DateTimeParseException) {
            // Log the exception or display an error message
            "Invalid Date Format"
        }
    } else {
        "No Date Provided"
    }

}