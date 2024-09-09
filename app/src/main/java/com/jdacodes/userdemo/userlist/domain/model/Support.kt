package com.jdacodes.userdemo.userlist.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Support(
    val text: String,
    val url: String
) : Parcelable
