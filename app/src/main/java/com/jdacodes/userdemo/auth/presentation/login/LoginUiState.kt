package com.jdacodes.userdemo.auth.presentation.login

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val form: LoginForm = LoginForm()
)
data class LoginForm(
    val email: String = "eve.holt@reqres.in",
    val emailError: String? = null,
    val password: String = "cityslicka",
    val passwordError: String? = null,
    val rememberMe: Boolean = false
)

