package com.jdacodes.userdemo.userlist.presentation

import com.jdacodes.userdemo.userlist.domain.model.User

data class UserListState(
    val userList: List<User> = emptyList(),
    val isLoading: Boolean = false
)
