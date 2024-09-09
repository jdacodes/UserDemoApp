package com.jdacodes.userdemo.userlist.data.remote.dto

import com.jdacodes.userdemo.userlist.data.local.entity.UserListEntity

data class UserListDto(
    val data: List<UserDto>,
    val page: Int,
    val perPage: Int,
    val support: SupportDto,
    val total: Int,
    val totalPages: Int

) {

    fun toUserListEntity(): UserListEntity {
        return UserListEntity(
            data = UserDto.toUserList(data),
            page = page,
            perPage = perPage,
            support = support.toSupport(),
            total = total,
            totalPages = totalPages

        )
    }
}