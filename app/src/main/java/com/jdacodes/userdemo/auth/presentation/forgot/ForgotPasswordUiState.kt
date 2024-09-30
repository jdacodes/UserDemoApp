package com.jdacodes.userdemo.auth.presentation.forgot

data class ForgotPasswordUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val form: ForgotPasswordForm = ForgotPasswordForm()
)

data class ForgotPasswordForm(
    val email: String = "",
    val emailError: String? = null,
)