package com.jdacodes.userdemo.auth.domain.use_case

import com.jdacodes.userdemo.auth.data.remote.request.LoginRequest
import com.jdacodes.userdemo.auth.domain.model.LoginResult
import com.jdacodes.userdemo.auth.domain.repository.LoginRepository
import com.jdacodes.userdemo.core.utils.Resource

class LoginCase(
    private val loginRepository: LoginRepository,
    private val validateEmailUseCase: ValidateEmailCase,
    private val validatePasswordUseCase: ValidatePasswordCase
) {
    suspend fun execute(email: String, password: String, rememberMe: Boolean): LoginResult {
        // Validate email
        val emailValidation = validateEmailUseCase.execute(email)
        if (emailValidation is Resource.Error) {
//            return Resource.Error(emailValidation.message ?: "Invalid email")
            return LoginResult(
                emailError = emailValidation.message ?: "Invalid email"
            )
        }

        // Validate password
        val passwordValidation = validatePasswordUseCase.execute(password)
        if (passwordValidation is Resource.Error) {
//            return Resource.Error(passwordValidation.message ?: "Invalid password")
            return LoginResult(
                passwordError = passwordValidation.message ?: "Invalid password"
            )
        }
        val loginRequest = LoginRequest(
            username = email.trim(),
            password = password.trim()
        )
        // Proceed with login
//        return loginRepository.login(LoginRequest(email, password), rememberMe)
        return LoginResult(result = loginRepository.login(loginRequest, rememberMe))
    }
}
