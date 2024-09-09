package com.jdacodes.userdemo.auth.domain.use_case

import com.jdacodes.userdemo.auth.domain.repository.LoginRepository
import com.jdacodes.userdemo.core.utils.Resource

class LogoutCase (
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return loginRepository.logout()
    }
}