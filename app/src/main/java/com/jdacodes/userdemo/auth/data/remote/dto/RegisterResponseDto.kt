package com.jdacodes.userdemo.auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterResponseDto(
    @SerializedName("token")
    val token: String,
    @SerializedName("id")
    val id: Int,
)
