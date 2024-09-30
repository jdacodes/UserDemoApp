package com.jdacodes.userdemo.auth.domain.use_case

import com.jdacodes.userdemo.auth.util.EmailValidator
import com.jdacodes.userdemo.core.utils.Resource

class ValidateEmailCase (private val emailValidator: EmailValidator){
    fun execute(email: String): Resource<Unit> {
        return when {
            email.isBlank() -> Resource.Error("Email cannot be empty")
            !emailValidator.isValid(email) -> Resource.Error("Invalid email address")
            else -> Resource.Success(Unit)
        }
    }
}
