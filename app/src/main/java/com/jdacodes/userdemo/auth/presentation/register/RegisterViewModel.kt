package com.jdacodes.userdemo.auth.presentation.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdacodes.userdemo.auth.domain.use_case.RegisterCase
import com.jdacodes.userdemo.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerCase: RegisterCase
) : ViewModel() {
    // StateFlow for UI state
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // Channel for one-time events
    private val _eventChannel = Channel<RegisterEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

    private val _eventFormChannel = Channel<RegisterFormEvent>()
    val eventFormFlow = _eventFormChannel.receiveAsFlow()

    private fun resetFormErrorStates() {
        _uiState.update { currentState ->
            currentState.copy(
                form = currentState.form.copy(
                    emailError = null,
                    passwordError = null,
                    confirmError = null
                )
            )
        }
    }

    fun register(email: String, password: String, confirm: String) {
        viewModelScope.launch {
            Log.d("RegisterViewModel", "Registration started for email: $email")
            _uiState.update { it.copy(isLoading = true) }
            resetFormErrorStates()
            val loginResult = registerCase.execute(email, password, confirm)

//            var validationErrorMessage: String? = null

            // Check for validation errors
            loginResult.emailError?.let { error ->
                Log.d("RegisterViewModel", "Email error: $error")
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        form = currentState.form.copy(
                            emailError = error
                        )
                    )
                }
                _eventFormChannel.send(RegisterFormEvent.EmailChanged(error))
                return@launch // Exit early if there are validation errors
            }
            loginResult.passwordError?.let { error ->
                Log.d("RegisterViewModel", "Password error: $error")
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        form = currentState.form.copy(
                            passwordError = error
                        )
                    )
                }
                _eventFormChannel.send(RegisterFormEvent.PasswordChanged(error))
                return@launch // Exit early if there are validation errors
            }

            loginResult.confirmError?.let { error ->
                Log.d("RegisterViewModel", "Confirm password error: $error")
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        form = currentState.form.copy(
                            confirmError = error
                        )
                    )
                }
                _eventFormChannel.send(RegisterFormEvent.ConfirmChanged(error))
                return@launch // Exit early if there are validation errors
            }

            when (loginResult.result) {
                is Resource.Success -> {
                    // Emit a success event for one-time actions like navigation
                    Log.d("RegisterViewModel", "Registration successful")
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    _eventChannel.send(
                        RegisterEvent.RegisterSuccess(
                            message = "Registration Successful"
                        )
                    )
                }

                is Resource.Error -> {
                    Log.d("RegisterViewModel", "Registration error: ${loginResult.result.message}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                    _eventChannel.send(
                        RegisterEvent.RegisterFailure(
                            message = loginResult.result.message ?: "Registration failed"
                        )
                    )
                }

                else -> {
                    Log.d("RegisterViewModel", "Unknown error occurred")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Unknown error occurred"
                        )
                    }
                    _eventChannel.send(
                        RegisterEvent.RegisterFailure(
                            message = "Unknown error occurred"
                        )
                    )
                }
            }
        }
    }

    fun onFormEvent(event: RegisterFormEvent) {
        when (event) {
            is RegisterFormEvent.EmailChanged -> {
                // Update the email in the form
                _uiState.update { currentState ->
                    currentState.copy(
                        form = currentState.form.copy(
                            email = event.email
                        )
                    )
                }
            }

            is RegisterFormEvent.PasswordChanged -> {
                // Update the password in the form
                _uiState.update { currentState ->
                    currentState.copy(
                        form = currentState.form.copy(
                            password = event.password
                        )
                    )
                }
            }

            is RegisterFormEvent.ConfirmChanged -> {
                // Update the password in the form
                _uiState.update { currentState ->
                    currentState.copy(
                        form = currentState.form.copy(
                            confirm = event.confirm
                        )
                    )
                }
            }


            RegisterFormEvent.Submit -> {
                // Handle form submission logic
                // This might involve validation and triggering a login process
                // Example:
                val email = _uiState.value.form.email
                val password = _uiState.value.form.password
                val confirm = _uiState.value.form.confirm

                if (email.isNotBlank() && password.isNotBlank() && confirm.isNotBlank()) {
                    register(email, password, confirm)
                } else {
                    // Handle form validation errors here if needed
                    Log.d("RegisterViewModel", "RegisterFormEvent.Submit error")
                }
            }
        }
    }
}

// One-time events for the ViewModel
sealed class RegisterEvent {
    data class RegisterSuccess(val message: String) : RegisterEvent()
    data class RegisterFailure(val message: String) : RegisterEvent()
    data class ValidationError(val message: String?) : RegisterEvent()
}