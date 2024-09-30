package com.jdacodes.userdemo.auth.presentation.forgot

sealed class ForgotPasswordFormEvent {
    data class EmailChanged(val email: String) : ForgotPasswordFormEvent()
    object Submit: ForgotPasswordFormEvent()
}