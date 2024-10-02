package com.jdacodes.userdemo.profile.data.remote

import com.jdacodes.userdemo.profile.data.remote.dto.ProfileDto
import com.jdacodes.userdemo.profile.data.remote.request.ProfileRequest
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ProfileApiService {
    @PATCH("api/users/{id}")
    suspend fun updateProfile(
        @Path("id") id: Int,
        @Body profileRequest: ProfileRequest
    ): ProfileDto
}
