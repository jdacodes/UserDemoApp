package com.jdacodes.userdemo.profile.domain.repository

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getUserProfile(): Flow<String>
}