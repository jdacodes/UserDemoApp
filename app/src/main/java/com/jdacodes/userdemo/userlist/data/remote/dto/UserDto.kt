package com.jdacodes.userdemo.userlist.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.jdacodes.userdemo.userlist.data.local.entity.UserEntity
import com.jdacodes.userdemo.userlist.domain.model.User

data class UserDto(
    val avatar: String,
    val email: String,
    @SerializedName("first_name") val firstName: String,
    val id: Int,
    @SerializedName("last_name") val lastName: String
) {
    companion object {
        fun toUserList(userDtoList: List<UserDto>): List<User> {
            return userDtoList.map { dto ->
                User(
                    avatar = dto.avatar,
                    email = dto.email,
                    firstName = dto.firstName,
                    id = dto.id,
                    lastName = dto.lastName
                )
            }
        }

    }

    fun toUser(): User {
        return User(
            avatar = avatar,
            email = email,
            firstName = firstName,
            id = id,
            lastName = lastName
        )
    }

    fun toUserEntity(): UserEntity {
        return UserEntity(
            id = id,
            avatar = avatar,
            email = email,
            firstName = firstName,
            lastName = lastName
        )
    }
}
