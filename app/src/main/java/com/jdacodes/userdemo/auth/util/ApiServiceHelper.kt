package com.jdacodes.userdemo.auth.util

import android.util.Log
import com.google.gson.Gson
import com.jdacodes.userdemo.auth.data.remote.dto.ErrorResponseDto

fun parseErrorMessage(errorBody: String?): String {
    return try {
        // Assuming the error format is {"error": "Some message"}
        val errorResponse = Gson().fromJson(errorBody, ErrorResponseDto::class.java)
        errorResponse?.error ?: "An Unknown error occurred"
    } catch (e: Exception) {
        Log.e("ApiServiceHelper", "Error parsing error message: ${e.message}")
        "An Unknown error occurred"
    }
}
