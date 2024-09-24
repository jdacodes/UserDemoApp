package com.jdacodes.userdemo.auth.data.remote

import com.jdacodes.userdemo.auth.data.remote.dto.LoginResponseDto
import com.jdacodes.userdemo.auth.data.remote.dto.RegisterResponseDto
import com.jdacodes.userdemo.auth.data.remote.request.LoginRequest
import com.jdacodes.userdemo.auth.data.remote.request.RegistrationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponseDto>

    @POST("api/register")
    suspend fun registerUser(
        @Body registrationRequest: RegistrationRequest
    ): Response<RegisterResponseDto>
}