package com.jdacodes.userdemo.dashboard.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Support(
    val text: String,
    val url: String
) : Parcelable
