package com.jdacodes.userdemo.dashboard.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Color(
    val color: String = "",
    val id: Int = 0,
    val name: String = "",
    val pantoneValue: String = "",
    val year: Int = 0
) : Parcelable
