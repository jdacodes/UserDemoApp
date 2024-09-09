package com.jdacodes.userdemo.userlist.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserList(
    val userList: List<User>,
    val page: Int,
    val perPage: Int,
    val support: Support,
    val total: Int,
    val totalPages: Int
): Parcelable
