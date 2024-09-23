package com.jdacodes.userdemo.auth.domain.use_case

import com.jdacodes.userdemo.auth.data.remote.request.RegistrationRequest
import com.jdacodes.userdemo.auth.domain.model.RegisterResult
import com.jdacodes.userdemo.auth.domain.repository.RegisterRepository
import com.jdacodes.userdemo.core.utils.Resource

class RegisterCase (
    private val registerRepository: RegisterRepository,
    private val validateEmailUseCase: ValidateEmailCase,
    private val validatePasswordUseCase: ValidatePasswordCase,
    private val validateConfirmCase: ValidateConfirmCase
) {
    suspend fun execute(email: String, password: String, confirm: String): RegisterResult {
        // Validate email
        val emailValidation = validateEmailUseCase.execute(email)
        if (emailValidation is Resource.Error) {
            return RegisterResult(
                emailError = emailValidation.message ?: "Invalid email"
            )
        }

        // Validate password
        val passwordValidation = validatePasswordUseCase.execute(password)
        if (passwordValidation is Resource.Error) {
            return RegisterResult(
                passwordError = passwordValidation.message ?: "Invalid password"
            )
        }

        val confirmValidation = validateConfirmCase.execute(password, confirm)
        if (confirmValidation is Resource.Error) {
            return RegisterResult(
                confirmError = confirmValidation.message ?: "Password did not match"
            )
        }
        val regRequest = RegistrationRequest(
            username = email.trim(),
            password = password.trim()
        )
        // Proceed with register
        return RegisterResult(result = registerRepository.register(regRequest))
    }
}