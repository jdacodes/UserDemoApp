package com.jdacodes.userdemo.auth.presentation.register

import com.jdacodes.userdemo.auth.presentation.login.LoginFormEvent

sealed class RegisterFormEvent {
    data class EmailChanged(val email: String) : RegisterFormEvent()
    data class PasswordChanged(val password: String) : RegisterFormEvent()
    data class ConfirmChanged(val confirm: String) : RegisterFormEvent()
    object Submit : RegisterFormEvent()
}