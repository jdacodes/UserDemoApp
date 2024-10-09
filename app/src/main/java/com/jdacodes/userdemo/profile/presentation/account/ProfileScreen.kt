package com.jdacodes.userdemo.profile.presentation.account

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogoutSuccess: (String) -> Unit,
    onLogoutFailure: (String) -> Unit,
    onDeleteSuccess: (String) -> Unit,
    onDeleteFailure: (String) -> Unit,
    onClickUpdateProfile: (Int) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(key1 = true, block = {
        viewModel.getProfile()
    })
    val user = viewModel.profileState.value
    val userId = viewModel.profileState.value.id

    LaunchedEffect(key1 = true, block = {
        viewModel.getProfileInfo()
    })

    val profile by viewModel.uiState.collectAsStateWithLifecycle()


    ProfileScreenContent(
        viewModel = viewModel,
        user = user,
        profile = profile,
        onClickUpdateProfile = {
            if (userId != null) {
                onClickUpdateProfile(userId)
            }
        },
        onClickLogout = { viewModel.logout() },
        onLogoutSuccess = onLogoutSuccess,
        onLogoutFailure = onLogoutFailure,
        onClickDelete = {
                viewModel.deleteProfile(userId = userId)

        },
        onDeleteSuccess = onDeleteSuccess,
        onDeleteFailure = onDeleteFailure,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    )
}

