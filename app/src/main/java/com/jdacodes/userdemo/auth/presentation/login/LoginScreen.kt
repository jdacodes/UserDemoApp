package com.jdacodes.userdemo.auth.presentation.login

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.SoftwareKeyboardController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    uiState: LoginUiState,
    onLoginSuccess: (String) -> Unit,
    onLoginFailure: (String) -> Unit,
    onClickDontHaveAccount: () -> Unit,
    onClickForgotPassword: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    keyboardController: SoftwareKeyboardController
) {
    val emailState = uiState.form.email
    val passwordState = uiState.form.password
    val rememberMeState = uiState.form.rememberMe

    LoginScreenContent(
        modifier = modifier,
        viewModel = viewModel,
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onClickLogin = {viewModel.onFormEvent(LoginFormEvent.Submit)},
        onLoginSuccess = onLoginSuccess,
        onLoginFailure = onLoginFailure,
        onEmailTextChange = { viewModel.onFormEvent(LoginFormEvent.EmailChanged(it)) },
        onPasswordTextChange = { viewModel.onFormEvent(LoginFormEvent.PasswordChanged(it)) },
        onRememberMeClicked = { viewModel.onFormEvent(LoginFormEvent.RememberMeChanged(it))},
        onClickForgotPassword = onClickForgotPassword,
        onClickDontHaveAccount = onClickDontHaveAccount,
        emailState = emailState,
        passwordState = passwordState,
        rememberMeState = rememberMeState,
        keyboardController = keyboardController
    )

}



