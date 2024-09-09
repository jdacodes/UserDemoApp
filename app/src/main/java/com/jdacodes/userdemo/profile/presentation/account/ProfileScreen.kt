package com.jdacodes.userdemo.profile.presentation.account

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogoutSuccess: (String) -> Unit,
    onLogoutFailure: (String) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(key1 = true, block = {
        viewModel.getProfile()
    })
    val user = viewModel.profileState.value

    ProfileScreenContent(
        viewModel = viewModel,
        user = user,
        onClickLogout = { viewModel.logout()},
        onLogoutSuccess = onLogoutSuccess,
        onLogoutFailure = onLogoutFailure,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    )
}