package com.jdacodes.userdemo.dashboard.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ColorListDto(
    @SerializedName("data")
    val data: List<ColorDto>,
    @SerializedName("page")
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("support")
    val support: SupportDto,
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)