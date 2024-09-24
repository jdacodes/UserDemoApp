package com.jdacodes.userdemo.auth.data.util

import androidx.core.util.PatternsCompat
import com.jdacodes.userdemo.auth.util.EmailValidator

class EmailValidatorImpl: EmailValidator {
    override fun isValid(email: String): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }
}