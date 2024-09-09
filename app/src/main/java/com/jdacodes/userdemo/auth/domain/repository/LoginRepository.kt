package com.jdacodes.userdemo.auth.domain.repository

import com.jdacodes.userdemo.auth.data.remote.request.LoginRequest
import com.jdacodes.userdemo.auth.domain.model.LoginResult
import com.jdacodes.userdemo.core.utils.Resource

interface LoginRepository {
    suspend fun login(loginRequest: LoginRequest, rememberMe: Boolean): Resource<Unit>
    suspend fun autoLogin(): Resource<Unit>
    suspend fun logout(): Resource<Unit>
}