package com.jdacodes.userdemo.userlist.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdacodes.userdemo.userlist.domain.model.SingleUserResponse
import com.jdacodes.userdemo.userlist.domain.model.Support
import com.jdacodes.userdemo.userlist.domain.model.User

@Entity
data class SingleUserResponseEntity(
    @PrimaryKey val id: Int,
    val data: User,
    val support: Support
) {
    fun toSingleUserResponse(): SingleUserResponse {
        return SingleUserResponse(
            data = data,
            support = support
        )
    }
}

