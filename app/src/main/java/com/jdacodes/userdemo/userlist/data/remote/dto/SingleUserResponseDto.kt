package com.jdacodes.userdemo.userlist.data.remote.dto

import com.jdacodes.userdemo.userlist.data.local.entity.SingleUserResponseEntity
import com.jdacodes.userdemo.userlist.data.local.entity.UserEntity

data class SingleUserResponseDto(
    val data: UserDto,
    val support: SupportDto
) {
    fun toUserEntity(): UserEntity {
        return data.toUserEntity()
    }

    fun toSingleUserResponseEntity(): SingleUserResponseEntity {
        return SingleUserResponseEntity(
            id = data.id,
            data = data.toUser(),
            support = support.toSupport()
        )

    }
}

