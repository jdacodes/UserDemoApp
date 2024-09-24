package com.jdacodes.userdemo.auth.domain.use_case

import com.jdacodes.userdemo.auth.util.EmailValidator
import com.jdacodes.userdemo.core.utils.Resource

class ValidateEmailCase (private val emailValidator: EmailValidator){
    fun execute(email: String): Resource<Unit> {
        return if (email.isNotEmpty() && emailValidator.isValid(email)) {
            Resource.Success(Unit)
        } else {
            Resource.Error("Invalid email address")
        }
    }
}
