package com.jdacodes.userdemo.userlist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jdacodes.userdemo.core.utils.Resource
import com.jdacodes.userdemo.core.utils.UiEvents
import com.jdacodes.userdemo.userlist.domain.model.User
import com.jdacodes.userdemo.userlist.domain.use_case.GetSingleUserCase
import com.jdacodes.userdemo.userlist.domain.use_case.GetUserListCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
class UserViewModel @Inject constructor(
    private val getUserListCase: GetUserListCase,
    private val getSingleUserCase: GetSingleUserCase
) : ViewModel() {

    private val _uiListState = MutableStateFlow(UserListState())
    val uiListState: StateFlow<UserListState> = _uiListState.asStateFlow()

    private val _uiDetailState = MutableStateFlow(UserDetailState())
    val uiDetailState: StateFlow<UserDetailState> = _uiDetailState.asStateFlow()

    private var userListJob: Job? = null
    private var userDetailJob: Job? = null

    private val _uiEventChannel = Channel<UiEvents>()
    val uiEvents = _uiEventChannel.receiveAsFlow()

    private val _users = MutableStateFlow<PagingData<User>>(PagingData.empty())
    val users: StateFlow<PagingData<User>> = _users.asStateFlow()


    init {
        loadUsers()
    }

     fun loadUsers() {
        viewModelScope.launch {
            getUserListCase().cachedIn(viewModelScope).collectLatest { pagingData ->
                _users.value = pagingData
            }
        }
    }

    fun getUserById(userId: Int) {
        userDetailJob?.cancel()
        userDetailJob = viewModelScope.launch {
            getSingleUserCase(userId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiDetailState.update { currentState ->
                            currentState.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        // Extract the User from SingleUserResponse
                        val user = result.data
                        _uiDetailState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                user = user,
//                                errorMessage = null
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiDetailState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                user = null
                            )
                        }
                        _uiEventChannel.send(
                            UiEvents.SnackBarEvent(
                                message = result.message ?: "\"An unknown error occurred"
                            )
                        )
                    }
                }
            }
        }
    }


}