package com.jdacodes.userdemo.auth.data.remote.request

import com.google.gson.annotations.SerializedName

data class RegistrationRequest(
    @SerializedName("email")
    val username: String,
    @SerializedName("password")
    val password: String,
)
