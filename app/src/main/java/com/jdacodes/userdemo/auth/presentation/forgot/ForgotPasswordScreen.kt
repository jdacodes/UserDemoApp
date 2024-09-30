package com.jdacodes.userdemo.auth.presentation.forgot

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel,
    uiState: ForgotPasswordUiState,
    onResetSuccess: (String) -> Unit,
    onResetFailure: (String) -> Unit,
    onBackClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    keyboardController: SoftwareKeyboardController
) {
    val emailState = uiState.form.email

    ForgotPasswordContent(
        modifier = modifier,
        viewModel = viewModel,
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onClickReset = { viewModel.onFormEvent(ForgotPasswordFormEvent.Submit) },
        onResetSuccess = onResetSuccess,
        onResetFailure = onResetFailure,
        emailState = emailState,
        onEmailTextChange = { viewModel.onFormEvent(ForgotPasswordFormEvent.EmailChanged(it)) },
        onBackClick = onBackClick,
        keyboardController = keyboardController
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordContent(
    modifier: Modifier,
    viewModel: ForgotPasswordViewModel,
    uiState: ForgotPasswordUiState,
    snackbarHostState: SnackbarHostState,
    onClickReset: () -> Unit,
    onResetSuccess: (String) -> Unit,
    onResetFailure: (String) -> Unit,
    emailState: String,
    onEmailTextChange: (String) -> Unit,
    onBackClick: () -> Unit,
    keyboardController: SoftwareKeyboardController
) {
    val emailFocusRequester = remember { FocusRequester() }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Forgot Password") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {

            val outlineTextFieldColors = OutlinedTextFieldDefaults.colors(

                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary,
                errorBorderColor = MaterialTheme.colorScheme.error,
                selectionColors = TextSelectionColors(
                    handleColor = MaterialTheme.colorScheme.primary,
                    backgroundColor = MaterialTheme.colorScheme.primary
                )

            )
            LazyColumn(contentPadding = paddingValues) {
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Column {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(emailFocusRequester),
                            value = emailState,
                            onValueChange = {
                                onEmailTextChange(it)
                            },
                            label = {
                                Text(
                                    text = "Email",
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            ).copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController.hide()
                                }),
                            maxLines = 1,
                            singleLine = true,
                            isError = uiState.form.emailError != null,
                            colors = outlineTextFieldColors
                        )
                        if (uiState.form.emailError != null) {
                            Text(
                                text = uiState.form.emailError ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = onClickReset,
                        shape = AbsoluteRoundedCornerShape(40.dp),
                        enabled = !uiState.isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                        )
                    ) {
                        Text(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            text = "Reset Password",
                            textAlign = TextAlign.Center
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
    LaunchedEffect(key1 = viewModel.eventFlow) {
        viewModel.eventFlow.collectLatest { event ->
            Log.d("ForgotPasswordScreenContent", "Event received: $event")
            when (event) {
                is ForgotPasswordEvent.ForgotPassSuccess -> {
                    Log.d("ForgotPasswordScreenContent", "Password reset success")
                    onResetSuccess(event.message)
                }

                is ForgotPasswordEvent.ForgotPassFailure -> {
                    Log.d("RegisterScreenContent", "Resetting password failed: ${event.message}")
                    onResetFailure(event.message)
                }


            }
        }
    }

}
