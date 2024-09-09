package com.jdacodes.userdemo.auth.domain.use_case

import com.jdacodes.userdemo.core.utils.Resource

class ValidatePasswordCase {
    fun execute(password: String): Resource<Unit> {
        return if (password.length >= 6) {
            Resource.Success(Unit)
        } else {
            Resource.Error("Password must be at least 6 characters long")
        }
    }
}
