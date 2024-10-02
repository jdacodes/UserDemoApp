package com.jdacodes.userdemo.profile.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProfileDto(
    @SerializedName("job")
    val job: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("updatedAt")
    val updatedAt: String = ""
)