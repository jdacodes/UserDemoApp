package com.jdacodes.userdemo.auth.data.remote

import com.jdacodes.userdemo.auth.data.remote.dto.LoginResponseDto
import com.jdacodes.userdemo.auth.data.remote.request.LoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): LoginResponseDto
}