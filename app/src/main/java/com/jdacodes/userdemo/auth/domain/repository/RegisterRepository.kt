package com.jdacodes.userdemo.auth.domain.repository

import com.jdacodes.userdemo.auth.data.remote.request.RegistrationRequest
import com.jdacodes.userdemo.core.utils.Resource

interface RegisterRepository {
    suspend fun register(registrationRequest: RegistrationRequest): Resource<Unit>
}