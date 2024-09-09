package com.jdacodes.userdemo.userlist.data.remote

import com.jdacodes.userdemo.userlist.data.remote.dto.SingleUserResponseDto
import com.jdacodes.userdemo.userlist.data.remote.dto.UserListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {

    @GET("api/users")
    suspend fun getUsers(
        @Query("page") page: Int
    ): UserListDto

    @GET("users/{id}")
    suspend fun getUser(
        @Path("id") id: Int
    ): SingleUserResponseDto
}