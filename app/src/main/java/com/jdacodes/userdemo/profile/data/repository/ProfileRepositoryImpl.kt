package com.jdacodes.userdemo.profile.data.repository

import com.jdacodes.userdemo.auth.data.local.AuthPreferences
import com.jdacodes.userdemo.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class ProfileRepositoryImpl(private val authPreferences: AuthPreferences) : ProfileRepository {
    override fun getUserProfile(): Flow<String> {
        return authPreferences.getUserData
    }
}