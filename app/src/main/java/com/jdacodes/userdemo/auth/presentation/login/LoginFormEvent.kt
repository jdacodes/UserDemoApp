package com.jdacodes.userdemo.auth.presentation.login

sealed class LoginFormEvent {
    data class EmailChanged(val email: String) : LoginFormEvent()
    data class PasswordChanged(val password: String) : LoginFormEvent()
    data class RememberMeChanged(val rememberMe: Boolean) : LoginFormEvent()
    object Submit: LoginFormEvent()
}