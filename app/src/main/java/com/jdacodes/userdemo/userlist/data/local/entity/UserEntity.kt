package com.jdacodes.userdemo.userlist.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdacodes.userdemo.userlist.domain.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val avatar: String?,
    val email: String?,
    val firstName: String?,
    val lastName: String?
) {
    fun toUser(): User {
        return User(
            avatar = avatar,
            email = email,
            firstName = firstName,
            id = id,
            lastName = lastName
        )
    }
}
