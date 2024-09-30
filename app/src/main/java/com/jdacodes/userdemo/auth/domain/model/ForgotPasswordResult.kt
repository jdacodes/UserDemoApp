package com.jdacodes.userdemo.auth.domain.model

import com.jdacodes.userdemo.core.utils.Resource

data class ForgotPasswordResult(
    val emailError: String? = null,
    val result: Resource<Unit>? = null
)
