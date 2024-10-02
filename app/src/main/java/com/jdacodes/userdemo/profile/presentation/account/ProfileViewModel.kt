package com.jdacodes.userdemo.profile.presentation.account

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jdacodes.userdemo.auth.domain.use_case.LogoutCase
import com.jdacodes.userdemo.core.utils.Resource
import com.jdacodes.userdemo.profile.data.remote.dto.ProfileDto
import com.jdacodes.userdemo.profile.data.remote.request.ProfileRequest
import com.jdacodes.userdemo.profile.domain.model.Profile
import com.jdacodes.userdemo.profile.domain.model.User
import com.jdacodes.userdemo.profile.domain.repository.ProfileRepository
import com.jdacodes.userdemo.profile.domain.toDomain
import com.jdacodes.userdemo.userlist.data.remote.dto.UserDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val logoutCase: LogoutCase,
    private val gson: Gson
) : ViewModel() {


    // StateFlow for UI state
    private val _uiState = MutableStateFlow(UpdateProfileUiState())
    val uiState: StateFlow<UpdateProfileUiState> = _uiState.asStateFlow()

    private val _profileState = mutableStateOf(User())
    val profileState: State<User> = _profileState

    private val _infoState = mutableStateOf(Profile())
    val infoState: State<Profile> = _infoState

    // Channel for one-time events
    private val _eventChannel = Channel<LogoutEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

    private val _eventUpdateChannel = Channel<UpdateEvent>()
    val eventUpdateFlow = _eventUpdateChannel.receiveAsFlow()

    private val _eventFormChannel = Channel<UpdateProfileUiState>()
    val eventFormChannel = _eventFormChannel.receiveAsFlow()

    fun getProfile() {
        viewModelScope.launch {
            profileRepository.getUserProfile().collectLatest { data ->
                Log.d("ProfileViewModel", "Data: $data")
                val userResponse = gson.fromJson(data, UserDto::class.java)
                _profileState.value = userResponse.toDomain()
            }
        }
    }

    fun getProfileInfo() {
        viewModelScope.launch {
            profileRepository.getProfileInfo().collectLatest { data ->
                Log.d("ProfileViewModel", "Data: $data")

                // Ensure data is not null or empty
                if (data.isNullOrEmpty()) {
                    Log.e("ProfileViewModel", "Received empty or null data")
                    return@collectLatest // Exit if there's no data
                }

                val profileResponse = try {
                    gson.fromJson(data, ProfileDto::class.java)
                } catch (e: Exception) {
                    Log.e("ProfileViewModel", "JSON parsing error: ${e.message}")
                    return@collectLatest // Exit if parsing fails
                }

                // Make sure to handle potential nulls
                val profile = profileResponse.toDomain()
                _uiState.update { currentState ->
                    currentState.copy(
                        form = currentState.form.copy(
                            name = profile.name.takeIf { it.isNotBlank() } ?: "",
                            job = profile.job.takeIf { it.isNotBlank() } ?: "",
                            updatedAt = profile.updatedAt.takeIf { it.isNotBlank() } ?: ""
                        )
                    )
                }
            }
        }
    }


    fun logout() {
        viewModelScope.launch {
            val result = logoutCase()
            Log.d("ProfileViewModel", "Result: ${result.message}")
            when (result) {
                is Resource.Success -> {
                    Log.d("ProfileViewModel", "Logout success")
                    _eventChannel.send(
                        LogoutEvent.LogoutSuccess(
                            message = "Logout success"
                        )
                    )
                }

                is Resource.Error -> {
                    Log.d("ProfileViewModel", "Logout failed")
                    _eventChannel.send(
                        LogoutEvent.LogoutFailure(
                            message = "Logout failed"
                        )
                    )
                }

                else -> {}
            }
        }
    }

    fun update(userId: Int, name: String, job: String, updatedAt: String) {
        viewModelScope.launch {
            Log.d("ProfileViewModel", "Update Profile with userId: $userId")
            _uiState.update { it.copy(isLoading = true) }
            resetFormErrorStates()
            val result = profileRepository.updateProfileInfo(
                id = userId,
                ProfileRequest(
                    name = name,
                    job = job
                )
            )
            Log.d("ProfileViewModel", "Update Result: ${result.message}")
            when (result) {
                is Resource.Success -> {
                    Log.d("ProfileViewModel", "Update success")
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    _eventUpdateChannel.send(
                        UpdateEvent.UpdateSuccess(
                            message = "Update profile success"
                        )
                    )
                }

                is Resource.Error -> {
                    Log.d("ProfileViewModel", "Update failed")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                    _eventUpdateChannel.send(
                        UpdateEvent.UpdateFailure(
                            message = "Update profile failed"
                        )
                    )
                }

                else -> {
                    Log.d("ProfileViewModel", "Unknown error occurred")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                    _eventUpdateChannel.send(
                        UpdateEvent.UpdateFailure(
                            message = "Unknown error occurred"
                        )
                    )
                }
            }
        }
    }

    fun onFormEvent(event: UpdateProfileFormEvent) {
        when (event) {
            is UpdateProfileFormEvent.NameChanged -> {
                // Update the email in the form
                _uiState.update { currentState ->
                    currentState.copy(
                        form = currentState.form.copy(
                            name = event.name,
                        )
                    )
                }
                setNameError(null)
            }

            is UpdateProfileFormEvent.JobChanged -> {
                // Update the email in the form
                _uiState.update { currentState ->
                    currentState.copy(
                        form = currentState.form.copy(
                            job = event.job,
                        )
                    )
                }
                setNameError(null)
            }

            is UpdateProfileFormEvent.UpdatedAtChanged -> {
                // Update the email in the form
                _uiState.update { currentState ->
                    currentState.copy(
                        form = currentState.form.copy(
                            updatedAt = event.updatedAt,
                        )
                    )
                }
                setNameError(null)
            }

            is UpdateProfileFormEvent.Submit -> {
                val name = _uiState.value.form.name
                val job = _uiState.value.form.job
                val updatedAt = _uiState.value.form.updatedAt
                val userId = event.userId


                var hasError = false

                if (name.isBlank()) {
                    setNameError("Name cannot be empty")
                    hasError = true
                } else {
                    setNameError(null)
                }

                if (job.isBlank()) {
                    setJobError("Work cannot be empty")
                    hasError = true
                } else {
                    setJobError(null)
                }

                if (!hasError) {
                    update(
                        userId = userId!!,
                        name = name,
                        job = job,
                        updatedAt = updatedAt
                    )
                } else {
                }
            }


        }
    }

    private fun resetFormErrorStates() {
        _uiState.update { currentState ->
            currentState.copy(
                form = currentState.form.copy(
                    nameError = null,
                    jobError = null,
                    updatedAtError = null
                )
            )
        }
    }

    private fun setNameError(error: String?) {
        _uiState.update { currentState ->
            currentState.copy(
                form = currentState.form.copy(
                    nameError = error
                )
            )
        }
    }

    private fun setJobError(error: String?) {
        _uiState.update { currentState ->
            currentState.copy(
                form = currentState.form.copy(
                    jobError = error
                )
            )
        }
    }

    private fun setUpdateAtError(error: String?) {
        _uiState.update { currentState ->
            currentState.copy(
                form = currentState.form.copy(
                    updatedAtError = error
                )
            )
        }
    }
}

sealed class LogoutEvent {
    data class LogoutSuccess(val message: String) : LogoutEvent()
    data class LogoutFailure(val message: String) : LogoutEvent()
}

