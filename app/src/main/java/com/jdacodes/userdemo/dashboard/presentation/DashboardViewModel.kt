package com.jdacodes.userdemo.dashboard.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.google.gson.Gson
import com.jdacodes.userdemo.core.utils.Resource
import com.jdacodes.userdemo.core.utils.UiEvents
import com.jdacodes.userdemo.dashboard.domain.model.Color
import com.jdacodes.userdemo.dashboard.domain.use_case.GetColorListCase
import com.jdacodes.userdemo.dashboard.domain.use_case.GetSingleColorCase
import com.jdacodes.userdemo.dashboard.domain.use_case.GetUserProfileCase
import com.jdacodes.userdemo.profile.domain.model.User
import com.jdacodes.userdemo.profile.domain.toDomain
import com.jdacodes.userdemo.userlist.data.remote.dto.UserDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getColorListCase: GetColorListCase,
    private val getSingleColorCase: GetSingleColorCase,
    private val getUserProfileCase: GetUserProfileCase,
    private val gson: Gson
) : ViewModel() {

    private val _uiListState = MutableStateFlow(ColorListState())
    val uiListState: StateFlow<ColorListState> = _uiListState.asStateFlow()

    private val _uiDetailState = MutableStateFlow(ColorDetailState())
    val uiDetailState: StateFlow<ColorDetailState> = _uiDetailState.asStateFlow()

    private val _profileState = mutableStateOf(User())
    val profileState: State<User> = _profileState

    private var colorListJob: Job? = null
    private var colorDetailJob: Job? = null
    private var userProfileJob: Job? = null

    private val _uiEventChannel = Channel<UiEvents>()
    val uiEvents = _uiEventChannel.receiveAsFlow()

    private val _colors = MutableStateFlow<PagingData<Color>>(PagingData.empty())
    val colors: StateFlow<PagingData<Color>> = _colors.asStateFlow()

    var searchQuery = mutableStateOf("")

    init {
        loadColors()
    }

    private fun loadColors() {
        colorListJob?.cancel()
         colorDetailJob = viewModelScope.launch {
            getColorListCase().cachedIn(viewModelScope).collectLatest { pagingData ->
                _colors.value = pagingData
            }
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        searchQuery.value = newQuery
        loadFilteredColors()
    }

    private fun loadFilteredColors() {
        colorListJob?.cancel()
        colorListJob = viewModelScope.launch {
            getColorListCase()
                .cachedIn(viewModelScope)
                .map { pagingData ->
                    pagingData.filter { it.name.contains(searchQuery.value, ignoreCase = true) }
                }.collectLatest { filteredPagingData ->
                    _colors.value = filteredPagingData
                }
        }
    }

    fun getColorById(colorId: Int) {
        colorDetailJob?.cancel()
        colorDetailJob = viewModelScope.launch {
            getSingleColorCase(colorId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiDetailState.update { currentState ->
                            currentState.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        // Extract the Color from Single Color response
                        val color = result.data
                        _uiDetailState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                color = color,
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiDetailState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                color = null
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

    fun getProfile() {
        userProfileJob?.cancel()
        userProfileJob = viewModelScope.launch {
            getUserProfileCase.invoke().collectLatest { data ->
                val userResponse = gson.fromJson(data, UserDto::class.java)
                _profileState.value = userResponse.toDomain()
            }
        }
    }
}