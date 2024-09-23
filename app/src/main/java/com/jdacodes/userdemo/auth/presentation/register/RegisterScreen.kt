package com.jdacodes.userdemo.auth.presentation.register

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    uiState: RegisterUiState,
    onRegisterSuccess: (String) -> Unit,
    onRegisterFailure: (String) -> Unit,
    onBackClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val emailState = uiState.form.email
    val passwordState = uiState.form.password
    val confirmState = uiState.form.confirm

    RegisterScreenContent(
        modifier = modifier,
        viewModel = viewModel,
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onClickRegister = { viewModel.onFormEvent(RegisterFormEvent.Submit) },
        onRegisterSuccess = onRegisterSuccess,
        onRegisterFailure = onRegisterFailure,
        emailState = emailState,
        passwordState = passwordState,
        confirmState = confirmState,
        onEmailTextChange = { viewModel.onFormEvent(RegisterFormEvent.EmailChanged(it)) },
        onPasswordTextChange = { viewModel.onFormEvent(RegisterFormEvent.PasswordChanged(it)) },
        onConfirmTextChange = { viewModel.onFormEvent(RegisterFormEvent.ConfirmChanged(it))},
        onBackClick = onBackClick
    )


}