package com.jdacodes.userdemo.userlist.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.jdacodes.userdemo.core.utils.UiEvents
import com.jdacodes.userdemo.userlist.presentation.composables.UserListElement
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UserScreen(
    viewModel: UserViewModel,
    uiState: UserListState,
    snackbarHostState: SnackbarHostState,
    onClickUser: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    UserListElement(
        viewModel = viewModel,
        onClickUser = onClickUser,
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        modifier = modifier,

        )

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collectLatest { event ->
            when (event) {
                is UiEvents.SnackBarEvent -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                is UiEvents.NavigateEvent -> {
                }
            }
        }
    }


}