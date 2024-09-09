package com.jdacodes.userdemo.auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ErrorResponseDto(
    @SerializedName("error")
    val error: String
)
