package com.jdacodes.userdemo.auth.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdacodes.userdemo.auth.domain.use_case.LoginCase
import com.jdacodes.userdemo.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginCase
) : ViewModel() {

    // StateFlow for UI state
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Channel for one-time events
    private val _eventChannel = Channel<LoginEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

    private val _eventFormChannel = Channel<LoginFormEvent>()
    val eventFormFlow = _eventFormChannel.receiveAsFlow()

    private fun resetFormErrorStates() {
        _uiState.update { currentState ->
            currentState.copy(
                form = currentState.form.copy(
                    emailError = null,
                    passwordError = null
                )
            )
        }
    }

    private fun setEmailError(error: String?) {
        _uiState.update { currentState ->
            currentState.copy(
                form = currentState.form.copy(
                    emailError = error
                )
            )
        }
    }

    private fun setPasswordError(error: String?) {
        _uiState.update { currentState ->
            currentState.copy(
                form = currentState.form.copy(
                    passwordError = error
                )
            )
        }
    }

    fun login(email: String, password: String, rememberMe: Boolean) {
        viewModelScope.launch {
            Log.d("LoginViewModel", "Login started for email: $email")
            _uiState.update { it.copy(isLoading = true) }
            resetFormErrorStates()
            val loginResult = loginUseCase.execute(email, password, rememberMe)

//            var validationErrorMessage: String? = null

            // Check for validation errors
            loginResult.emailError?.let { error ->
                Log.d("LoginViewModel", "Email error: $error")
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        form = currentState.form.copy(
                            emailError = error
                        )
                    )
                }
                _eventFormChannel.send(LoginFormEvent.EmailChanged(error))
                return@launch // Exit early if there are validation errors
            }
            loginResult.passwordError?.let { error ->
                Log.d("LoginViewModel", "Password error: $error")
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        form = currentState.form.copy(
                            passwordError = error
                        )
                    )
                }
                _eventFormChannel.send(LoginFormEvent.PasswordChanged(error))
                return@launch // Exit early if there are validation errors
            }

            when (loginResult.result) {
                is Resource.Success -> {
                    // Emit a success event for one-time actions like navigation
                    Log.d("LoginViewModel", "Login successful")
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    _eventChannel.send(
                        LoginEvent.LoginSuccess(
                            message = "Login Successful"
                        )
                    )
                }

                is Resource.Error -> {
                    Log.d("LoginViewModel", "Login error: ${loginResult.result.message}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            form = it.form.copy(
                                emailError = loginResult.result.message ?: "Login failed"
                            )
                        )
                    }
                    _eventChannel.send(
                        LoginEvent.LoginFailure(
                            message = loginResult.result.message ?: "Login failed"
                        )
                    )
                }

                else -> {
                    Log.d("LoginViewModel", "Unknown error occurred")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            form = it.form.copy(emailError = "Unknown error occurred")
                        )
                    }
                    _eventChannel.send(
                        LoginEvent.LoginFailure(
                            message = "Unknown error occurred"
                        )
                    )
                }
            }
        }
    }

    fun onFormEvent(event: LoginFormEvent) {
        when (event) {
            is LoginFormEvent.EmailChanged -> {
                // Update the email in the form
                _uiState.update { currentState ->
                    currentState.copy(
                        form = currentState.form.copy(
                            email = event.email
                        )
                    )
                }
                setEmailError(null)
            }

            is LoginFormEvent.PasswordChanged -> {
                // Update the password in the form
                _uiState.update { currentState ->
                    currentState.copy(
                        form = currentState.form.copy(
                            password = event.password
                        )
                    )
                }
                setPasswordError(null)
            }

            is LoginFormEvent.RememberMeChanged -> {
                // Update the rememberMe checkbox in the form
                _uiState.update { currentState ->
                    currentState.copy(
                        form = currentState.form.copy(
                            rememberMe = event.rememberMe
                        )
                    )
                }
            }

            LoginFormEvent.Submit -> {
                // Handle form submission logic
                // This might involve validation and triggering a login process
                // Example:
                val email = _uiState.value.form.email
                val password = _uiState.value.form.password
                val rememberMe = _uiState.value.form.rememberMe

                var hasError = false

                if (email.isBlank()) {
                    setEmailError("Email cannot be empty")
                    hasError = true
                } else {
                    setEmailError(null)
                }

                if (password.isBlank()) {
                    setPasswordError("Password cannot be empty")
                    hasError = true
                } else {
                    setPasswordError(null)
                }

                if (!hasError) {
                    login(email, password, rememberMe)
                }
            }
        }
    }

}

// One-time events for the ViewModel
sealed class LoginEvent {
    data class LoginSuccess(val message: String) : LoginEvent()
    data class LoginFailure(val message: String) : LoginEvent()
    data class ValidationError(val message: String?) : LoginEvent()
}
