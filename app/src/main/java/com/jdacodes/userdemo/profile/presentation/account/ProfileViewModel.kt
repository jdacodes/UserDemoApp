package com.jdacodes.userdemo.profile.presentation.account

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jdacodes.userdemo.auth.domain.use_case.LogoutCase
import com.jdacodes.userdemo.auth.presentation.login.LoginEvent
import com.jdacodes.userdemo.core.utils.Resource
import com.jdacodes.userdemo.profile.domain.model.User
import com.jdacodes.userdemo.profile.domain.repository.ProfileRepository
import com.jdacodes.userdemo.profile.domain.toDomain
import com.jdacodes.userdemo.userlist.data.remote.dto.UserDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val logoutCase: LogoutCase,
    private val gson: Gson
): ViewModel() {

    private val _profileState = mutableStateOf(User())
    val profileState: State<User> = _profileState

    // Channel for one-time events
    private val _eventChannel = Channel<LogoutEvent>()
    val eventFlow = _eventChannel.receiveAsFlow()

    fun getProfile() {
        viewModelScope.launch {
            profileRepository.getUserProfile().collectLatest { data ->
                Log.d("ProfileViewModel", "Data: $data")
                val userResponse = gson.fromJson(data, UserDto::class.java)
                _profileState.value = userResponse.toDomain()
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            val result = logoutCase()
            Log.d("ProfileViewModel", "Result: ${result.message}")
            when(result) {
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
}
sealed class LogoutEvent {
    data class LogoutSuccess(val message: String) : LogoutEvent()
    data class LogoutFailure(val message: String) : LogoutEvent()
}