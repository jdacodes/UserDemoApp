package com.jdacodes.userdemo.profile.data.remote.request

import com.google.gson.annotations.SerializedName

data class ProfileRequest(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("job")
    val job: String? = null,
)
