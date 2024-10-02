package com.jdacodes.userdemo.profile.data.repository

import android.util.Log
import com.jdacodes.userdemo.auth.data.local.AuthPreferences
import com.jdacodes.userdemo.core.utils.Resource
import com.jdacodes.userdemo.profile.data.remote.ProfileApiService
import com.jdacodes.userdemo.profile.data.remote.dto.ProfileDto
import com.jdacodes.userdemo.profile.data.remote.request.ProfileRequest
import com.jdacodes.userdemo.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

class ProfileRepositoryImpl(
    private val authPreferences: AuthPreferences,
    private val profileApiService: ProfileApiService
) : ProfileRepository {
    override fun getUserProfile(): Flow<String> {
        return authPreferences.getUserData
    }

    override suspend fun updateProfileInfo(
        id: Int,
        profileRequest: ProfileRequest
    ): Resource<Unit> {
        return try {
            val response = profileApiService.updateProfile(id = id, profileRequest = profileRequest)
            val updatedAt = response.updatedAt
            if (updatedAt != null) {
                authPreferences.saveProfileData(
                    ProfileDto(
                        job = response.job,
                        name = response.name,
                        updatedAt = updatedAt
                    )
                )
                Resource.Success(Unit)
            } else {
                Resource.Error(message = "Something went wrong while updating your Profile")
            }


        } catch (e: IOException) {
            Log.d("ProfileRepositoryImpl", "Update error: ${e.message}")
            Resource.Error(message = "Could not reach the server, please check your internet connection!")
        } catch (e: HttpException) {
            Log.d("ProfileRepositoryImpl", "Update error: ${e.message}")
            Resource.Error(message = "An Unknown error occurred, please try again!")
        }

    }

    override suspend fun getProfileInfo(): Flow<String> {
        return authPreferences.getProfileData
    }
}