package com.jdacodes.userdemo.auth.domain.use_case

import android.util.Patterns
import com.jdacodes.userdemo.core.utils.Resource

class ValidateEmailCase {
    // TODO: Change implementation for Testing 
    fun execute(email: String): Resource<Unit> {
        return if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Resource.Success(Unit)
        } else {
            Resource.Error("Invalid email address")
        }
    }
}
