package com.jdacodes.userdemo.auth.domain.use_case

import com.jdacodes.userdemo.auth.domain.model.ForgotPasswordResult
import com.jdacodes.userdemo.auth.domain.model.RegisterResult
import com.jdacodes.userdemo.core.utils.Resource

class ForgotPasswordCase(
    private val validateEmailUseCase: ValidateEmailCase
) {
    suspend operator fun invoke(email: String): ForgotPasswordResult {
        // Validate email
        val emailValidation = validateEmailUseCase.execute(email)
        if (emailValidation is Resource.Error) {
            return ForgotPasswordResult(
                emailError = emailValidation.message ?: "Invalid email"
            )
        }
        //Automatically have a success for result since Forgot Password api does not exist
        return ForgotPasswordResult(result = Resource.Success(Unit))
    }
}