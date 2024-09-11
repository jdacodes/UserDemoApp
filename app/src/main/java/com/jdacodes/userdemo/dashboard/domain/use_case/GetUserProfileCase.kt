package com.jdacodes.userdemo.dashboard.domain.use_case

import com.jdacodes.userdemo.dashboard.domain.repository.ColorRepository
import kotlinx.coroutines.flow.Flow

class GetUserProfileCase(private val repository: ColorRepository) {
    operator fun invoke(): Flow<String> {
        return repository.getUserProfile()
    }
}