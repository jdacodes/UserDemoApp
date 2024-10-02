package com.jdacodes.userdemo.auth.presentation.forgot

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdacodes.userdemo.auth.domain.use_case.ForgotPasswordCase
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
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordCase: ForgotPasswordCase
) : ViewModel() {

    // StateFlow for UI state
    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    // Channel for one-time events
    private val _eventChannel = Channel<ForgotPasswordEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

    private val _eventFormChannel = Channel<ForgotPasswordFormEvent>()
    val eventFormFlow = _eventFormChannel.receiveAsFlow()

    private fun resetFormErrorStates() {
        _uiState.update { currentState ->
            currentState.copy(
                form = currentState.form.copy(
                    emailError = null,
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

    fun resetPassword(email: String) {
        viewModelScope.launch {
            Log.d("ForgotPasswordViewModel", "Reset Password started for email: $email")
            _uiState.update { it.copy(isLoading = true) }
            resetFormErrorStates()
            val forgotPassResult = forgotPasswordCase.invoke(email)

//            var validationErrorMessage: String? = null

            // Check for validation errors
            forgotPassResult.emailError?.let { error ->
                Log.d("ForgotPasswordViewModel", "Email error: $error")
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        form = currentState.form.copy(
                            emailError = error
                        )
                    )
                }
                _eventFormChannel.send(ForgotPasswordFormEvent.EmailChanged(error))
                return@launch // Exit early if there are validation errors
            }

            when (forgotPassResult.result) {
                is Resource.Success -> {
                    // Emit a success event for one-time actions like navigation
                    Log.d("ForgotPasswordViewModel", "Reset password successful")
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    _eventChannel.send(
                        ForgotPasswordEvent.ForgotPassSuccess(
                            message = "Reset Password successful but API does not exist"
                        )
                    )
                }

                is Resource.Error -> {
                    Log.d(
                        "ForgotPasswordViewModel",
                        "Forgot password error: ${forgotPassResult.result.message}"
                    )
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            form = it.form.copy(emailError = forgotPassResult.result.message)
                        )
                    }
                    _eventChannel.send(
                        ForgotPasswordEvent.ForgotPassFailure(
                            message = forgotPassResult.result.message ?: "Reset password failed"
                        )
                    )
                }

                else -> {
                    Log.d("ForgotPasswordViewModel", "Unknown error occurred")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            form = it.form.copy(emailError = "Unknown error occurred"),
                        )
                    }
                    _eventChannel.send(
                        ForgotPasswordEvent.ForgotPassFailure(
                            message = "Unknown error occurred"
                        )
                    )
                }
            }
        }
    }

    fun onFormEvent(event: ForgotPasswordFormEvent) {
        when (event) {
            is ForgotPasswordFormEvent.EmailChanged -> {
                // Update the email in the form
                _uiState.update { currentState ->
                    currentState.copy(
                        form = currentState.form.copy(
                            email = event.email,
                        )
                    )
                }
                setEmailError(null)
            }


            ForgotPasswordFormEvent.Submit -> {
                val email = _uiState.value.form.email


                if (email.isBlank()) {
                    setEmailError("Email cannot be empty")
                } else {
                    resetPassword(email)
                }
            }
        }
    }

}

// One-time events for the ViewModel
sealed class ForgotPasswordEvent {
    data class ForgotPassSuccess(val message: String) : ForgotPasswordEvent()
    data class ForgotPassFailure(val message: String) : ForgotPasswordEvent()
}