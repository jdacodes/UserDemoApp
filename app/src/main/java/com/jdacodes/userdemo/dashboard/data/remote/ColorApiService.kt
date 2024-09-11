package com.jdacodes.userdemo.dashboard.data.remote

import com.jdacodes.userdemo.dashboard.data.remote.dto.ColorListDto
import com.jdacodes.userdemo.dashboard.data.remote.dto.SingleColorResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ColorApiService {
    @GET("api/unknown")
    suspend fun getColors(
        @Query("page") page: Int
    ): ColorListDto

    @GET("api/unknown/{id}")
    suspend fun getColor(
        @Path("id") id: Int
    ): SingleColorResponseDto
}