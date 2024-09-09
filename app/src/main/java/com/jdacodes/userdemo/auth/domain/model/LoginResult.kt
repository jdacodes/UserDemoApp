package com.jdacodes.userdemo.auth.domain.model

import com.jdacodes.userdemo.core.utils.Resource

data class LoginResult(
    val passwordError: String? = null,
    val emailError: String? = null,
    val result: Resource<Unit>? = null
)
