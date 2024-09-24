package com.jdacodes.userdemo.auth.util

interface EmailValidator {
    fun isValid(email: String): Boolean
}