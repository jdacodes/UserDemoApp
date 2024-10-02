package com.jdacodes.userdemo.profile.presentation.account

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.SoftwareKeyboardController

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogoutSuccess: (String) -> Unit,
    onLogoutFailure: (String) -> Unit,
    onClickUpdateProfile: (Int) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(key1 = true, block = {
        viewModel.getProfile()
    })
    val user = viewModel.profileState.value
    val userId = viewModel.profileState.value.id



    ProfileScreenContent(
        viewModel = viewModel,
        user = user,
        onClickUpdateProfile = {
            if (userId != null) {
                onClickUpdateProfile(userId)
            }
        },
        onClickLogout = { viewModel.logout() },
        onLogoutSuccess = onLogoutSuccess,
        onLogoutFailure = onLogoutFailure,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    )
}

