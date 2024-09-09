package com.jdacodes.userdemo.userlist.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val avatar: String?,
    val email: String?,
    val firstName: String?,
    val id: Int,
    val lastName: String?
):Parcelable
