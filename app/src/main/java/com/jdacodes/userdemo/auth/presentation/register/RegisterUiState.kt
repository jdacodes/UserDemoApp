package com.jdacodes.userdemo.auth.presentation.register

data class RegisterUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val form: RegisterForm = RegisterForm()
)

data class RegisterForm(
    val email: String = "eve.holt@reqres.in",
    val emailError: String? = null,
    val password: String = "pistol",
    val passwordError: String? = null,
    val confirm: String = "pistol",
    val confirmError: String? = null
)