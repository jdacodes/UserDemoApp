package com.jdacodes.userdemo.auth.presentation.splash

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.jdacodes.userdemo.auth.domain.use_case.AutoLoginCase
import com.jdacodes.userdemo.auth.util.Constants.SPLASH_SCREEN_DURATION
import com.jdacodes.userdemo.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val autoLoginCase: AutoLoginCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _eventState = savedStateHandle.getLiveData<Boolean>("eventState", false)
    val eventState: Flow<Boolean> = _eventState.asFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = savedStateHandle["eventState"] ?: false
    )

    init {
        viewModelScope.launch {
            delay(SPLASH_SCREEN_DURATION)
            autoLoginUser()
        }
    }

    private fun autoLoginUser() {
        viewModelScope.launch {
            when (autoLoginCase()) {
                is Resource.Success -> {
                    _eventState.value = true
                }

                is Resource.Error -> {
                    _eventState.value = false
                }

                else -> {}
            }
            _isLoading.value = false // Stop showing loading indicator once the auto-login process is complete
        }
    }
}