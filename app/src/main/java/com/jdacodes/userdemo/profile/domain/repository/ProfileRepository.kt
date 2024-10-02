package com.jdacodes.userdemo.profile.domain.repository

import com.jdacodes.userdemo.core.utils.Resource
import com.jdacodes.userdemo.profile.data.remote.request.ProfileRequest
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getUserProfile(): Flow<String>
    suspend fun updateProfileInfo(id: Int, profileRequest: ProfileRequest): Resource<Unit>
    suspend fun getProfileInfo(): Flow<String>
}