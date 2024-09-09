package com.jdacodes.userdemo.userlist.presentation

import com.jdacodes.userdemo.userlist.domain.model.User

data class UserDetailState(
    val isLoading: Boolean = false,
    val user: User? = null, // Directly use User instead of SingleUserResponse
)


