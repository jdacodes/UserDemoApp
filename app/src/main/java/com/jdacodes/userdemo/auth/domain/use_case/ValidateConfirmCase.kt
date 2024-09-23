package com.jdacodes.userdemo.auth.domain.use_case

import com.jdacodes.userdemo.core.utils.Resource

class ValidateConfirmCase {
    fun execute(password: String, confirm: String): Resource<Unit> {
        return if (password == confirm) {
            Resource.Success(Unit)
        } else {
            Resource.Error("Password did not match")
        }

    }
}