package com.jdacodes.userdemo.profile.domain

import com.jdacodes.userdemo.profile.data.remote.dto.ProfileDto
import com.jdacodes.userdemo.profile.domain.model.Profile
import com.jdacodes.userdemo.profile.domain.model.User
import com.jdacodes.userdemo.userlist.data.remote.dto.UserDto

internal fun UserDto.toDomain(): User {
    return User(
        avatar = avatar,
        email = email,
        firstName = firstName,
        id = id,
        lastName = lastName
    )
}

internal fun ProfileDto.toDomain(): Profile {
    return Profile(
        job = job,
        name = name,
        updatedAt = updatedAt

    )
}