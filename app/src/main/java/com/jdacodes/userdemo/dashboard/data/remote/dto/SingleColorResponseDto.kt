package com.jdacodes.userdemo.dashboard.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SingleColorResponseDto(
    @SerializedName("data")
    val `data`: ColorDto,
    @SerializedName("support")
    val support: SupportDto
)