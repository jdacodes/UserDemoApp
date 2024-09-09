package com.jdacodes.userdemo.userlist.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdacodes.userdemo.userlist.domain.model.User
import com.jdacodes.userdemo.userlist.domain.model.Support
import com.jdacodes.userdemo.userlist.domain.model.UserList

@Entity
data class UserListEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val data: List<User>,
    val page: Int,
    val perPage: Int,
    val support: Support,
    val total: Int,
    val totalPages: Int
) {
    fun toUserList(): UserList {
        return UserList(
            userList = data,
            page = page,
            perPage = perPage,
            support = support,
            total = total,
            totalPages = totalPages
        )
    }
}